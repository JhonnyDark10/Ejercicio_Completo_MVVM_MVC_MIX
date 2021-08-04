package com.demo.control.mix;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.demo.modelo.Agenda;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaAgendaEditar {
	@Wire
	private Window winPersonaAgendaEditar;

	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();

	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;
	private Agenda agenda;

	/**
	 * Metodo que emula al metodo doAfterComposer de MVC
	 */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
		agenda = (Agenda) Executions.getCurrent().getArg().get("Agenda");

	}


	/**
	 * Graba la informacion. Para poder cerrar la ventana, pasa como parametro
	 * desde el formulario la ventana: onClick="@command('grabar', ventana=winPersonaAgendaEditar)"
	 */
	@Command
	public void grabar(){

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
			salir();

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
	public void salir(){
		winPersonaAgendaEditar.detach();
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
