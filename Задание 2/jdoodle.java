//Класс Calculator
class Calculation implements Callable<Integer> {

    private final int input;

    public Calculation(int input) {
        this.input = input;
    }

    @Override
    public Integer call() throws Exception {
        Thread.sleep(4000);

        Integer num = input * input;
        System.out.println("Result: "+num);

        return num;
    }
}
