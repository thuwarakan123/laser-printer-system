public class Student implements Runnable{
    //use to store the student name
    private String name;
    private ThreadGroup studentGroup;
    private Printer printer;

    public Student(String name, ThreadGroup studentGroup, Printer printer) {
        this.name = name;
        this.studentGroup = studentGroup;
        this.printer = printer;
    }

    @Override
    public void run() {
        //list use to store documents
        Document [] document = new Document[5];

        //use to store the total count of printed document pages
        int totalPage = 0;

        //creating 5 documents for printing
        document[0] = new Document("Doc01", "CWK_01_"+this.name, 15);
        document[1] = new Document("Doc02", "CWK_02_"+this.name, 20);
        document[2] = new Document("Doc03", "CWK_03_"+this.name, 12);
        document[3] = new Document("Doc04", "CWK_04_"+this.name, 18);
        document[4] = new Document("Doc05", "CWK_05_"+this.name, 25);

        //loop the document array
        for (Document doc: document) {
            printer.printDocument(doc);
            totalPage = totalPage + doc.getNumberOfPages();

            //generate random number between 1 to 5
            int num = ((int)(Math.random() * 5 + 1));

            try {
                Thread.sleep(num * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(this.name+ " Finished Printing: 5 Documents, "+ totalPage + "Pages");
    }
}
