public class PrintingSystem {
    public static void main(String[] args) {
        //create thread groups for add students and technicians
        ThreadGroup studentGroup = new ThreadGroup("Student Group");
        ThreadGroup technicianGroup = new ThreadGroup("Technician Group");

        //creating the laser printer class
        ServicePrinter printer = new LaserPrinter("Laser Printer", 250, 500, 0, studentGroup);

        //creating a student class
        Runnable student_01 = new Student("Thuwarakan", studentGroup, printer);
        Runnable student_02 = new Student("Nirakulan", studentGroup, printer);
        Runnable student_03 = new Student("Mathuravan", studentGroup, printer);
        Runnable student_04 = new Student("Ashwin", studentGroup, printer);

        Thread student01 = new Thread(studentGroup, student_01, "Thuwarakan");
        Thread student02 = new Thread(studentGroup, student_02, "Nirakulan");
        Thread student03 = new Thread(studentGroup, student_03, "Mathuravan");
        Thread student04 = new Thread(studentGroup, student_04, "Ashwin");

        //creating a technicians group
        Runnable tonerTech = new TonerTechnician("Ramkumar", technicianGroup, printer);
        Thread tonerTechnician = new Thread(technicianGroup, tonerTech, "Ramkumar");

        Runnable paperTech = new PaperTechnician("Manojprashath", technicianGroup, printer);
        Thread paperTechnician = new Thread(technicianGroup, paperTech, "Manojprashath");

        student01.start();
        student02.start();
        student03.start();
        student04.start();

        tonerTechnician.start();
        paperTechnician.start();

        //to execute the end printing system
        try {
            student01.join();
            student02.join();
            student03.join();
            student04.join();

            tonerTechnician.join();
            paperTechnician.join();

            System.out.println("\nThe printing System successfully completed all the tasks!");
            System.out.println(printer.toString());
            System.out.println("\n------------------------------------------------ Thank you! ------------------------------------------------\n");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
