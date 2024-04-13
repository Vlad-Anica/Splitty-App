package client.scenes;
import client.utils.ServerUtils;
import jakarta.inject.Inject;

public class ExpenseOverviewCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private EventOverviewCtrl eventOverview;
    private long eventId;
    private long expenseId;

    @Inject
    public ExpenseOverviewCtrl(MainCtrl mainCtrl, ServerUtils server, EventOverviewCtrl eventOverview) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.eventOverview = eventOverview;
    }

    public void setup(Long eventId, Long expenseId){
        this.eventId = eventId;
        this.expenseId = expenseId;
    }

    public void fillRecieverName(){

    }

    public void setStandardData(){

    }

    public void setTagColor(){

    }

}
