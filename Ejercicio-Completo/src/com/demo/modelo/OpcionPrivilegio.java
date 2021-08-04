package com.demo.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the opcion_privilegio database table.
 * 
 */
@Entity
@Table(name="opcion_privilegio")
@NamedQuery(name="OpcionPrivilegio.findAll", query="SELECT o FROM OpcionPrivilegio o")
public class OpcionPrivilegio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_opcion")
	private Opcion opcion;

	//bi-directional many-to-one association to Privilegio
	@ManyToOne
	@JoinColumn(name="id_privilegio")
	private Privilegio privilegio;

	public OpcionPrivilegio() {
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

	public Opcion getOpcion() {
		return this.opcion;
	}

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}

	public Privilegio getPrivilegio() {
		return this.privilegio;
	}

	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}

}