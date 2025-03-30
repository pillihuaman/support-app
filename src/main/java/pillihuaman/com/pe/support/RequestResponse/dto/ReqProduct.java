package pillihuaman.com.pe.support.RequestResponse.dto;

import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.Date;



public class ReqProduct {
	
	private ObjectId idProduct;
	private Timestamp createDate;
	private String description;
	private Date expirationDate;
	private int idImagen;
	private int idPrice;
	private int idSystem;
	private int idType;
	private ObjectId idUser;
	private String name;
	private String status;
	private Timestamp updateDate;
	private String userCreate;
	private String userModify;

	public ObjectId getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(ObjectId idProduct) {
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
	public int getIdPrice() {
		return idPrice;
	}
	public void setIdPrice(int idPrice) {
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
	public ObjectId getIdUser() {
		return idUser;
	}
	public void setIdUser(ObjectId idUser) {
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


