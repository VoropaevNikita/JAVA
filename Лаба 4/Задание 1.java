public class Appointment {
    private String id;
    private String appointment;
    private int cabinetNumb;
    private String time;

    public Appointment(String id, String appointment, int cabinetNumb, String time) {
        this.id = id;
        this.appointment = appointment;
        this.cabinetNumb = cabinetNumb;
        this.time = time;
    }

    public String getId() {
        return id;}

    public String getAppointment() {
        return appointment;}

    public int getCabinetNumb() {
        return cabinetNumb;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Notification{ ID: " + id + " " + appointment + cabinetNumb + " at " + time + '}';
    }
}



@RestController
public class RsocketClientController {
    private static final String APPOINTMENT = "New Appointment at Cabinet ";
    private final RSocketRequester rSocketRequester;
    List<Appointment> appointments =new ArrayList<Appointment>();
    Logger logger = LoggerFactory.getLogger(RsocketClientController.class);

    public RsocketClientController(@Autowired RSocketRequester.Builder builder) {
        this.rSocketRequester = builder.tcp("localhost", 7000);
    }

    private int[] cabinets = {211, 212, 213, 214, 217, 218, 219};

    @GetMapping("/request-response") //add new appointment
    public Mono<Appointment> requestResponse() {
        String uniqueID = UUID.randomUUID().toString();
        int i = new Random().nextInt(cabinets.length);
              int hour = new Random().nextInt(20);
        int min = new Random().nextInt(60);
        String time = Integer.toString(hour)+":" + Integer.toString(min);
        Appointment newAppointment = new Appointment(uniqueID, APPOINTMENT, cabinets[i], time);
        appointments.add(newAppointment);
        logger.info("Send notification for my-request-response: " + newAppointment);
        return rSocketRequester
                .route("my-request-response")
                .data(newAppointment)
                .retrieveMono(Appointment.class);}
    @GetMapping("/fire-and-forget/{id}") //del appointment by id
    public Mono<Void> fireAndForget(@PathVariable String id) {
        appointments.removeIf(e -> e.getId().equals(id));
        logger.info("Send notification for my-fire-and-forget: Appointment with ID: " +id +" was deleted.");
        return rSocketRequester
                .route("my-fire-and-forget")
                .data(id)
                .retrieveMono(Void.class);
    }

    @GetMapping("/request-stream") //get all appointments
    public ResponseEntity<Flux<Long>> requestStream() {
        logger.info("Send notification for my-request-stream: full list of appointments");
        List<Integer> list = new ArrayList<>();
        for(int i=0; i< appointments.size(); i++){
            list.add(1);}
        Flux<Integer> fluxApps = Flux.fromIterable(list).map(Integer::valueOf);
        Flux<Long> numberOfNotifications = this.rSocketRequester
                .route("my-request-stream")
                .data(fluxApps)
                .retrieveFlux(Long.class);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(numberOfNotifications);
    }

    @GetMapping("/channel/{cabinetNumb}")//quantity by cabinetNum
    public ResponseEntity<Flux<Long>> channel(@PathVariable String cabinetNumb) {
        List<Appointment> foundAppointments = appointments.stream()
                .filter(findAp -> Integer.parseInt(cabinetNumb)==findAp.getCabinetNumb()).collect(Collectors.toList());
        List<Integer> list = new ArrayList<>();
        for(int i=0; i< foundAppointments.size(); i++){
            list.add(1);
        }
        Flux<Integer> fluxApps = Flux.fromIterable(list).map(Integer::valueOf);
        logger.info("Send notification for my-channel: Getting Appointments in Cabinet № " + cabinetNumb);
        Flux<Long> numberOfNotifications = this.rSocketRequester
                .route("my-channel")
                .data(fluxApps)
                .retrieveFlux(Long.class);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(numberOfNotifications);
    }
    @GetMapping("/close")
    public void close() {
        rSocketRequester.rsocketClient().dispose();
    }
}


@Controller
public class RsocketServerController {
    Logger logger = LoggerFactory.getLogger(RsocketServerController.class);
    List<Appointment> appointments =new ArrayList<Appointment>();

    @MessageMapping("my-request-response") //stream of 1
    public Appointment requestResponse(Appointment appointment) {
        logger.info("Received notification for my-request-response: " + appointment);
        appointments.add(appointment);
    return new Appointment(appointment.getId(), appointment.getAppointment(), appointment.getCabinetNumb(),
        appointment.getTime());
    }

    @MessageMapping("my-fire-and-forget") //no response
    public void fireAndForget(String id){
        appointments.removeIf(e -> e.getId().equals(id));
        logger.info("Received notification for my-fire-and-forget: delete appointment with id = " + id);
    }

    @MessageMapping("my-request-stream") //finite stream of many
    public Flux<Long> requestStream(Flux<Long> notifications) {
        final AtomicLong notificationCount = new AtomicLong(0);
        return notifications.doOnNext(appointment -> {
                    logger.info("Received notification for request-stream: " + appointment);
                    notificationCount.incrementAndGet();
                })
                .switchMap(appointment -> Flux.interval(Duration.ofSeconds(1)).map(new Object() {
                    private Function<Long, Long> numberOfMessages(AtomicLong notificationCount) {
                        long count = notificationCount.get();
                        logger.info("Return flux with count: " + count);
                        return i -> count;}
                }.numberOfMessages(notificationCount))).log();
    }

    @MessageMapping("my-channel") //bi-directional streams
    public Flux<Long> channel(Flux<Long> notifications) {
        final AtomicLong notificationCount = new AtomicLong(0);
        return notifications.doOnNext(appointment -> {
            logger.info("Received notification for channel: " + appointment);
            notificationCount.incrementAndGet();})
                .switchMap(appointment -> Flux.interval(Duration.ofSeconds(1)).map(new Object() {
                    private Function<Long, Long> numberOfMessages(AtomicLong notificationCount) {
                        long count = notificationCount.get();
                        logger.info("Return flux with count: " + count);
                        return i -> count;
                    }
                }.numberOfMessages(notificationCount))).log();}
}


@SpringBootTest
class MyRSocketServerApplicationTests {
    String Id = "TestId";
    String Appointment = "New Appointment at Cabinet ";
    int Numb = 217;
    String time = "10:30";

    private static RSocketRequester rSocketRequester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder, @Value("${spring.rsocket.server.port}") Integer port) {
        rSocketRequester = builder.tcp("localhost", port);
    }

    @Test
    void testRequestResponse() {
        Mono<Appointment> result = rSocketRequester
                .route("my-request-response")
                .data(new Appointment(Id, Appointment, Numb, time))
                .retrieveMono(Appointment.class);

        StepVerifier
                .create(result)
                .consumeNextWith(notification -> {
                    assertThat(notification.getId()).isEqualTo(Id);
                    assertThat(notification.getAppointment()).isEqualTo(Appointment);
                    assertThat(notification.getCabinetNumb()).isEqualTo(Numb);
                    assertThat(notification.getTime()).isEqualTo(time);
                })
                .verifyComplete();
    }

    @Test
    void testFireAndForget() {
        Mono<Void> result = rSocketRequester
                .route("my-fire-and-forget")
                .retrieveMono(Void.class);
        StepVerifier
                .create(result)
                .verifyComplete();
    }
}
