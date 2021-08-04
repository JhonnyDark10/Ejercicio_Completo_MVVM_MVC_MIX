package com.demo.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the privilegio database table.
 * 
 */
@Entity
@NamedQuery(name="Privilegio.findAll", query="SELECT p FROM Privilegio p")
public class Privilegio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String codigo;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to OpcionPrivilegio
	@OneToMany(mappedBy="privilegio")
	private List<OpcionPrivilegio> opcionPrivilegios;

	//bi-directional many-to-one association to UsuarioPrivilegio
	@OneToMany(mappedBy="privilegio")
	private List<UsuarioPrivilegio> usuarioPrivilegios;

	public Privilegio() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<OpcionPrivilegio> getOpcionPrivilegios() {
		return this.opcionPrivilegios;
	}

	public void setOpcionPrivilegios(List<OpcionPrivilegio> opcionPrivilegios) {
		this.opcionPrivilegios = opcionPrivilegios;
	}

	public OpcionPrivilegio addOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().add(opcionPrivilegio);
		opcionPrivilegio.setPrivilegio(this);

		return opcionPrivilegio;
	}

	public OpcionPrivilegio removeOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().remove(opcionPrivilegio);
		opcionPrivilegio.setPrivilegio(null);

		return opcionPrivilegio;
	}

	public List<UsuarioPrivilegio> getUsuarioPrivilegios() {
		return this.usuarioPrivilegios;
	}

	public void setUsuarioPrivilegios(List<UsuarioPrivilegio> usuarioPrivilegios) {
		this.usuarioPrivilegios = usuarioPrivilegios;
	}

	public UsuarioPrivilegio addUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().add(usuarioPrivilegio);
		usuarioPrivilegio.setPrivilegio(this);

		return usuarioPrivilegio;
	}

	public UsuarioPrivilegio removeUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().remove(usuarioPrivilegio);
		usuarioPrivilegio.setPrivilegio(null);

		return usuarioPrivilegio;
	}

}