import java.util.Observable;
import java.util.Observer;

public class Task1 {
    public static void main(String[] args){
        TempSensor tempSensor = new TempSensor();
        Temperature level = new Temperature();
        tempSensor.addObserver(level);
        CO2Sensor co2Sensor = new CO2Sensor();
        CO2 level1 = new CO2();
        co2Sensor.addObserver(level1);
        AlarmSystem alarmSystem = new AlarmSystem();
        Alarm alarm = new Alarm();
        alarmSystem.addObserver(alarm);
        long endTime = System.currentTimeMillis() + 1000;
        long startTime = System.currentTimeMillis();
        while (startTime < endTime) {
            try {
                tempSensor.changeMessage("Temperature changed!");
                co2Sensor.changeMessage("CO2 level changed!");
                if (level.getTemp() > 25 && level1.getCo() > 70)
                    alarmSystem.changeMessage("ALARM!");
                else if (level.getTemp() > 25)
                    alarmSystem.changeMessage("The level of temperature is high!");
                else if (level1.getCo() > 70)
                    alarmSystem.changeMessage("The level of CO2 is high!");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startTime = System.currentTimeMillis();
            endTime = System.currentTimeMillis() + 1000;
        }
    }
}

class TempSensor extends Observable {
    public void changeMessage(String message) {
        setChanged();
        notifyObservers(message);
    }
}

class Temperature implements Observer {
    int temp;
    @Override
    public void update(Observable o, Object arg){
        temp = (int)(Math.random()*(16)+15);
        System.out.println("\nTemperature now: " + temp);
    }
    public int getTemp() {
        return temp;
    }
}

class CO2Sensor extends Observable {
    public void changeMessage(String message){
        setChanged();
        notifyObservers(message);
    }
}

class CO2 implements Observer {
    int co;
    @Override
    public void update(Observable o, Object arg){
        co = (int)(Math.random()*(71)+30);
        System.out.println("CO2 now: " + co);
    }
    public int getCo() {
        return co;
    }
}

class AlarmSystem extends Observable {
    public void changeMessage(String message){
        setChanged();
        notifyObservers(message);
    }
}

class Alarm implements Observer {
    @Override
    public void update(Observable o, Object arg){
        System.out.println(arg);
    }
}
