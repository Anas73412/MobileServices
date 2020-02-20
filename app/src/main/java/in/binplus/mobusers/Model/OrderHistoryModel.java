package in.binplus.mobusers.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 22,January,2020
 */
public class OrderHistoryModel {
    String order_id,barcode,address,city,pincode,imei_no,brand,model,problem,instructions,status,order_at,name;

    public OrderHistoryModel() {
    }

    public OrderHistoryModel(String order_id, String barcode, String address, String city, String pincode, String imei_no, String brand, String model, String problem, String instructions, String status, String order_at, String name) {
        this.order_id = order_id;
        this.barcode = barcode;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.imei_no = imei_no;
        this.brand = brand;
        this.model = model;
        this.problem = problem;
        this.instructions = instructions;
        this.status = status;
        this.order_at = order_at;
        this.name = name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_at() {
        return order_at;
    }

    public void setOrder_at(String order_at) {
        this.order_at = order_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
