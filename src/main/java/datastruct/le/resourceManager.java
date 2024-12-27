package datastruct.le;

public class resourceManager {
    hashingTable hTable;

    public resourceManager(){
        hTable = new hashingTable();

        hTable.insert_QuadraticProbe(new Code("Fire","Firefighters, Paramedics" ));
        hTable.insert_QuadraticProbe(new Code("Theft","Police" ));
        hTable.insert_QuadraticProbe(new Code("Murder","Police, Paramedics" ));
        hTable.insert_QuadraticProbe(new Code("Hostage","Police" ));
        hTable.insert_QuadraticProbe(new Code("Shooting","Police, Paramedics" ));
        hTable.insert_QuadraticProbe(new Code("Building Collapse","Firefighters, Paramedics, Rescuers" ));
        hTable.insert_QuadraticProbe(new Code("Rescue Situation","Rescuers, paramedics" ));
        hTable.insert_QuadraticProbe(new Code("Animal Emergency","Firefighters, Emergency Veterinarians"));
    }

    public String localResourceLookUp(String code){
        return hTable.resourceLookUp(code);
    }
}
