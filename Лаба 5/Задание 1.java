spring.servlet.multipart.enabled=true
spring.servlet.multipart.file-size-threshold=2KB
spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=215MB


@RestController
public class HomeController {
	private static final String path = "./files/";
	@Autowired
	UploadDownloadService service;

	@PostMapping("/uploadFile")
	public ResponseEntity<List<String>>
	fileUpload(@RequestParam("file") MultipartFile file)
			throws Exception {
		return new ResponseEntity<>(service.uploadFile(file),
				HttpStatus.OK);
	}

	@RequestMapping(path = "/download", method = RequestMethod.GET)
	public ResponseEntity<Resource>
	download(@RequestParam String param) throws IOException {
		File file = new File(path + param);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource =
				new ByteArrayResource(Files.readAllBytes(path));
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename="+param);
		header.add("Cache-Control",
				"no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
                return ResponseEntity.ok().headers(header).
				contentLength(file.length())
             .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}
	
	@GetMapping("/getListOfFiles")
	public ResponseEntity<List<String>> getListOfFiles()
			throws Exception {
		return new ResponseEntity<>(service.getListofFiles(),
				HttpStatus.OK);
	}
}


@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}


@Service
public class UploadDownloadService {
	private static final String path = "./files";
	public List<String> uploadFile(MultipartFile file) throws Exception {
		if (!file.getOriginalFilename().isEmpty()) {
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(new File(path, file.getOriginalFilename())));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
		} else {
			throw new Exception();
		}
		List<String> list = new ArrayList<String>();
		File files = new File(path);
		String[] fileList = ((File) files).list();
		for (String name : fileList) {
			list.add(name);
}
		return list;
	}

	public List<String> getListofFiles() throws Exception {
		List<String> list = new ArrayList<String>();
		File files = new File(path);
		String[] fileList = ((File) files).list();
		for (String name : fileList) {
			list.add(name);
		}
		return list;
	}
}


FROM adoptopenjdk/openjdk11:latest
COPY out/artifacts/springboot_fileupload_filedownload_jar/springboot-fileupload-filedownload.jar demo.jar
RUN mkdir -p /files
CMD ["java", "-jar", "demo.jar"]


version: "3.9"
services:
  app1:
    build:
      dockerfile: src/Dockerfile
    image: pr5
    ports:
      - "8080:8080"
    restart: on-failure
    environment:
      - NAME_APP=app1
    volumes:
      - ../files:/files

  app2:
    build:
      dockerfile: src/Dockerfile
    image: pr5
    restart: on-failure
    environment:
      - NAME_APP=app2
    volumes:
      - ../files:/files
  app3:
    build:
      dockerfile: src/Dockerfile
    image: pr5
    restart: on-failure
    environment:
      - NAME_APP=app3
    volumes:
      - ../files:/files

  app4:
    build:
      dockerfile: src/Dockerfile
    image: pr5
    restart: on-failure
    environment:
      - NAME_APP=app4
    volumes:
      - ../files:/files

  proxy:
    image: nginx:1.11
    ports:
      - '80:80'
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf:ro


upstream myapp {
    server app1:8080;
    server app2:8080;
    server app3:8080;
    server app4:8080;
}

server {
    listen 80;
    location ^~ / {
        proxy_pass http://myapp;
    }
}
