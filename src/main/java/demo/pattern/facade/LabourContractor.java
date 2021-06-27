package demo.pattern.facade;

import demo.pattern.facade.subclasses.BrickLayer;
import demo.pattern.facade.subclasses.BrickWorker;
import demo.pattern.facade.subclasses.Mason;

/**
 * @author malaka
 * @create 2021-06-02 11:34
 */
public class LabourContractor {

    private Mason worker1 = new Mason();
    private BrickWorker worker2 = new BrickWorker();
    private BrickLayer worker3 = new BrickLayer();

    public void buildHouse(){
        worker1.mix();
        worker2.carry();
        worker3.neat();
    }
}
