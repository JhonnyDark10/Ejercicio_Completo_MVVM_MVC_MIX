package com.demo.control.mvc;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.demo.modelo.Agenda;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class PersonaAgendaEditar extends SelectorComposer {
	
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winPersonaAgendaEditar;
	
	// Contiene la ventana padre para invocar a la actualizacion de la misma cuando 
	// se cierra esta ventana.
	private PersonaLista personaLista; 
	
	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();
	
	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;
	private Agenda agenda;
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		// Recupera la ventana padre.
		personaLista = (PersonaLista)Executions.getCurrent().getArg().get("VentanaPadre");
		
		// Recupera la persona para gestionar los objetos desde la clase padre.
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
		
		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		agenda = null; 
		if (Executions.getCurrent().getArg().get("Agenda") != null) {
			agenda = (Agenda) Executions.getCurrent().getArg().get("Agenda");
		}else{
			agenda = new Agenda(); 
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
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#salir")
	public void salir(){
		winPersonaAgendaEditar.detach();
	}


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
