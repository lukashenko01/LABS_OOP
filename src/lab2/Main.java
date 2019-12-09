package lab2;

import db.DbAirServices;
import db.DbConfigs;
import db.DbExamples;

public class Main {
    public static void main(String[] args) throws Exception {
        DbExamples examples = new DbExamples(getServicesInstance());
        examples.runAll();
    }

    public static DbAirServices getServicesInstance() throws Exception {
        DbAirServices services = new DbAirServices();
        services.connect(DbConfigs.URL, DbConfigs.USER, DbConfigs.PASSWORD);
        return services;
    }
}
