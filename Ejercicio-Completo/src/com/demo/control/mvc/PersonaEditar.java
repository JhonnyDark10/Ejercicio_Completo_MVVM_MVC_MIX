package com.demo.control.mvc;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.demo.modelo.Pai;
import com.demo.modelo.PaiDAO;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaEditar extends SelectorComposer{
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winPersonaEditar;

	// Contiene la ventana padre para invocar a la actualizacion de la misma cuando 
	// se cierra esta ventana.
	private PersonaLista personaLista; 

	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();
	private PaiDAO paisDao = new PaiDAO();

	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;


	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		// Recupera la ventana padre.
		personaLista = (PersonaLista)Executions.getCurrent().getArg().get("VentanaPadre");
		
		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		persona = null; 
		if (Executions.getCurrent().getArg().get("Persona") != null) {
			persona = (Persona) Executions.getCurrent().getArg().get("Persona");
		}else{
			persona = new Persona(); 
		}
				
	}
	
	/**
	 * Escucha el evento "onClick" del objeto "grabar"
	 */
	@Listen("onClick=#grabar")
	public void grabar(){

		try {
			// Inicia la transaccion
			personaDao.getEntityManager().getTransaction().begin();
			
			// Almacena los datos.
			// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
			if (persona.getId() == null) {
				personaDao.getEntityManager().persist(persona);
			}else{
				persona = (Persona) personaDao.getEntityManager().merge(persona);
			}
			
			// Cierra la transaccion.
			personaDao.getEntityManager().getTransaction().commit();
			
			Clients.showNotification("Proceso Ejecutado con exito.");
			
			// Actualiza la lista
			personaLista.buscar();
			
			// Cierra la ventana
			salir();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// Si hay error, reversa la transaccion.
			personaDao.getEntityManager().getTransaction().rollback();
		}
		
	}
	
	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */
	public List<Pai> getPaises() {
		return paisDao.getPaises();
	}
	
	/**
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#salir")
	public void salir(){
		winPersonaEditar.detach();
	}
	
	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	
	
	
	
	
	
	
	
	
}
