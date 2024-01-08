public class LaserPrinter implements ServicePrinter{
    //used to store the printer name
    private String name;
    //used to store the current paper level of the printer
    private int paperLevel;
    //used to store the current toner level of the printer
    private int tonerLevel;
    //to keep track of printed document count
    private int documentPrintedCount;
    //keep track of student threads
    private ThreadGroup studentsGroup;
    //keep track of use count of refillPaper method
    private int refillPaperCount = 0;
    //keep track of use count of replaceTonerCartridge method
    private int replaceTonerCartridgeCount = 0;


    // use to initialize the printer class
    public LaserPrinter(String name, int paperLevel, int tonerLevel, int documentPrintedCount, ThreadGroup studentsGroup) {
        this.name = name;
        this.paperLevel = paperLevel;
        this.tonerLevel = tonerLevel;
        this.documentPrintedCount = documentPrintedCount;
        this.studentsGroup = studentsGroup;
    }

    //this method used to print the document
    @Override
    public synchronized void printDocument(Document document) {
        //to store the document total page count
        int totalPage = document.getNumberOfPages();

        while(!(totalPage <= tonerLevel && totalPage <= paperLevel)){
            if(totalPage > tonerLevel && totalPage > paperLevel){
                System.out.println("Insufficient Papers and Toner! Waiting to refill");
            }
            else if(totalPage > paperLevel){
                System.out.println("Insufficient Papers! Waiting to refill");
            }
            else{
                System.out.println("Insufficient Toner! Waiting to refill");
            }

            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.tonerLevel = this.tonerLevel - totalPage;
        this.paperLevel = this.paperLevel - totalPage;
        this.documentPrintedCount++;
        System.out.println("Printing => " + document.toString());
        notifyAll();
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        while(this.tonerLevel >= Minimum_Toner_Level){
            if(isAllStudentCompletePrinting()){
                System.out.println("All students finished printing, so technicians don't need to refill toner cartridge.");
                break;
            }
            try {
                    System.out.println("Toner has not reached the minimum level to be refilled. Waiting to check again!");
                    wait(5000);
            } catch (InterruptedException e) {
                    throw new RuntimeException(e);
            }
        }

        if(this.tonerLevel < Minimum_Toner_Level){
            System.out.print("Refilling toner cartridge... ");
            this.tonerLevel = PagesPerTonerCartridge;
            System.out.print("Successfully Refilled paper tray! \nNew toner Level is "+ this.tonerLevel+"\n");
            replaceTonerCartridgeCount = replaceTonerCartridgeCount + 1;
        }

        notifyAll();
    }

    @Override
    public synchronized void refillPaper() {
        while(!(this.paperLevel <= (Full_Paper_Tray - SheetsPerPack))){
            if(isAllStudentCompletePrinting()){
                System.out.println("All students finished printing, so technicians don't need to refill paper tray.");
                break;
            }
            try {
                    System.out.println("Refilling the paper will exceed the capacity. Waiting to check again!");
                    wait(5000);
            } catch (InterruptedException e) {
                    throw new RuntimeException(e);
            }
        }

        if(this.paperLevel <= (Full_Paper_Tray - SheetsPerPack)){
            System.out.print("Refilling paper tray... ");
            paperLevel = paperLevel + SheetsPerPack;
            System.out.print("Successfully Refilled paper tray! \nNew paper Level is "+ this.paperLevel+"\n");
            refillPaperCount = refillPaperCount + 1;
        }
        notifyAll();
    }

    //this function use to display the string message
    @Override
    public String toString() {
        return "[ " +
                "Printer Name: " + name +
                ", Paper Level:" + paperLevel +
                ", Toner Level:" + tonerLevel +
                ", Document Printed:" + documentPrintedCount +
                " ]";
    }

    public int getRefillPaperCount() {
        return refillPaperCount;
    }

    public int getReplaceTonerCartridgeCount() {
        return replaceTonerCartridgeCount;
    }

    //to check all the students finished the printing
    private boolean isAllStudentCompletePrinting() {
        return studentsGroup.activeCount() == 0;
    }
}
