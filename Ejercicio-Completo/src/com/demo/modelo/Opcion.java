package com.demo.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the opcion database table.
 * 
 */
@Entity
@NamedQuery(name="Opcion.findAll", query="SELECT o FROM Opcion o")
public class Opcion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	private String imagen;

	private String titulo;

	private String url;

	//bi-directional many-to-one association to OpcionPrivilegio
	@OneToMany(mappedBy="opcion")
	private List<OpcionPrivilegio> opcionPrivilegios;

	public Opcion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getImagen() {
		return this.imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<OpcionPrivilegio> getOpcionPrivilegios() {
		return this.opcionPrivilegios;
	}

	public void setOpcionPrivilegios(List<OpcionPrivilegio> opcionPrivilegios) {
		this.opcionPrivilegios = opcionPrivilegios;
	}

	public OpcionPrivilegio addOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().add(opcionPrivilegio);
		opcionPrivilegio.setOpcion(this);

		return opcionPrivilegio;
	}

	public OpcionPrivilegio removeOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().remove(opcionPrivilegio);
		opcionPrivilegio.setOpcion(null);

		return opcionPrivilegio;
	}

}