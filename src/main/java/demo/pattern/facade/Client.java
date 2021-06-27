package demo.pattern.facade;

/**
 * @author malaka
 * @create 2021-06-02 11:37
 */
public class Client {

    public static void main(String[] args) {
        LabourContractor labourContractor = new LabourContractor();
        labourContractor.buildHouse();
    }
}
