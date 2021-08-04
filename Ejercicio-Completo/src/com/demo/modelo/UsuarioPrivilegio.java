package com.demo.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the usuario_privilegio database table.
 * 
 */
@Entity
@Table(name="usuario_privilegio")
@NamedQuery(name="UsuarioPrivilegio.findAll", query="SELECT u FROM UsuarioPrivilegio u")
public class UsuarioPrivilegio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	//bi-directional many-to-one association to Privilegio
	@ManyToOne
	@JoinColumn(name="id_privilegio")
	private Privilegio privilegio;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	public UsuarioPrivilegio() {
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

	public Privilegio getPrivilegio() {
		return this.privilegio;
	}

	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}