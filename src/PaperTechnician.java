public class PaperTechnician implements Runnable{
    private String name;
    private ThreadGroup technicianGroup;
    private ServicePrinter printer;

    public PaperTechnician(String name, ThreadGroup technicianGroup, ServicePrinter printer) {
        this.name = name;
        this.technicianGroup = technicianGroup;
        this.printer = printer;
    }

    @Override
    public void run() {
       for(int i = 1; i <= 3; i++){

           printer.refillPaper();

           int num = ((int)(Math.random() * 5 + 1));
           try {
               Thread.sleep(num * 1000);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }

       System.out.println("Paper Technician Finished, packs of paper used: "+ ((LaserPrinter)printer).getRefillPaperCount());
    }
}
