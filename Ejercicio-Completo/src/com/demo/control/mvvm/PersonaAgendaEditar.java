package com.demo.control.mvvm;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.demo.modelo.Agenda;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaAgendaEditar {

	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();
	
	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;
	private Agenda agenda;
	
	/**
	 * Constructor para acceder a los objetos de trabajo.
	 */
	public PersonaAgendaEditar() {

	    // Recupera el objeto pasado como parametro. 
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
		agenda = (Agenda) Executions.getCurrent().getArg().get("Agenda");
		
	}
	
	/**
	 * Graba la informacion. Para poder cerrar la ventana, pasa como parametro
	 * desde el formulario la ventana: onClick="@command('grabar', ventana=winPersonaAgendaEditar)"
	 */
	@Command
	public void grabar(@BindingParam("ventana")  Window ventana){

		try {
			// Inicia la transaccion
			personaDao.getEntityManager().getTransaction().begin();
			
			// Si la agenda es nueva la agrega a la "coleccion" de agendas de la persona
			if (agenda.getId() == null) {
				persona.addAgenda(agenda);
			}
			
			// Almacena los cambios del objeto persona.
			personaDao.getEntityManager().merge(persona);
			
			// Cierra la transaccion.
			personaDao.getEntityManager().getTransaction().commit();
			
			Clients.showNotification("Proceso Ejecutado con exito.");
			
			// Actualiza la lista
			BindUtils.postGlobalCommand(null, null, "PersonaLista.buscar", null);
			
			// Cierra la ventana
			salir(ventana);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// Si hay error, reversa la transaccion.
			personaDao.getEntityManager().getTransaction().rollback();
		}
		
	}

	/**
	 * Cierra el formulario, para ello pasa un parametro desde el formulario con
	 * la ventana a cerrar: onClick="@command('salir', ventana=winPersonaAgendaEditar)"
	 */
	@Command
	public void salir(@BindingParam("ventana")  Window ventana){
		ventana.detach();
	}
	
	
	//Getters and Setters

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

}
