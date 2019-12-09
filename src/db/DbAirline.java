package db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class DbAirline implements Serializable {
    public int code;
    public String name;

    public DbAirline(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public DbAirline(DataInputStream in) throws Exception {
        code = in.readInt();
        name = in.readUTF();
    }

    public void serialize(DataOutputStream out) throws Exception {
        out.writeInt(code);
        out.writeUTF(name);
    }

    @Override
    public String toString() {
        return "Airline{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
