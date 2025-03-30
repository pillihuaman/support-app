package pillihuaman.com.pe.support.RequestResponse;

import java.sql.Timestamp;
import java.util.Date;

public class RespProduct {

	private String idProduct;
	private Timestamp createDate;
	private String description;
	private Date expirationDate;
	private int idImagen;
	private float idPrice;
	private int idSystem;
	private String userModify;
	private int idType;
	private String idUser;
	private String name;
	private String status;
	private Timestamp updateDate;
	private String userCreate;


	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	public float getIdPrice() {
		return idPrice;
	}
	public void setIdPrice(float idPrice) {
		this.idPrice = idPrice;
	}
	public int getIdSystem() {
		return idSystem;
	}
	public void setIdSystem(int idSystem) {
		this.idSystem = idSystem;
	}
	public int getIdType() {
		return idType;
	}
	public void setIdType(int idType) {
		this.idType = idType;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	public String getUserCreate() {
		return userCreate;
	}
	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}
	public String getUserModify() {
		return userModify;
	}
	public void setUserModify(String userModify) {
		this.userModify = userModify;
	}
	
	
}


