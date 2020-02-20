package in.binplus.mobusers.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 21,January,2020
 */
public class BarcodeDetailsModel {

    String id,bardcode,status,assigned_to,imei_no,activation_date,end_date;

    public BarcodeDetailsModel(String id, String bardcode, String status, String assigned_to, String imei_no, String activation_date, String end_date) {
        this.id = id;
        this.bardcode = bardcode;
        this.status = status;
        this.assigned_to = assigned_to;

        this.imei_no = imei_no;
        this.activation_date = activation_date;
        this.end_date = end_date;
    }

    public BarcodeDetailsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBardcode() {
        return bardcode;
    }

    public void setBardcode(String bardcode) {
        this.bardcode = bardcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
