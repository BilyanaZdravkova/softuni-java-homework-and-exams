import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {

    private static final String GRINGOTTS_PU = "gringotts";
    private static final String SALES_PU = "sales";
    private static final String UNIVERSITY_SYS_PU = "university_system";
    private static final String HOSPITAL_PU = "hospital";
    private static final String BILLS_PAYMENT_SYS_PU = "bills_payment_system";

    // TODO: Change username and password in persistence.xml before running. :)
    // TODO: There is a persistence-unit for each schema, please change it everywhere.
    // TODO: You don't need to change anything else, just run the App.

    public static void main(String[] args) {

        //TASK 1
        runEngine(GRINGOTTS_PU);
        //TASK 2
        runEngine(SALES_PU);
        //TASK 3
        runEngine(UNIVERSITY_SYS_PU);
        //TASK 4
        runEngine(HOSPITAL_PU);
        //TASK 5
        runEngine(BILLS_PAYMENT_SYS_PU);
    }

    private static void runEngine(String pUnit) {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory(pUnit);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Engine engine = new Engine(entityManager);
        engine.run();
    }
}
