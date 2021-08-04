package com.demo.control.mix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.demo.modelo.Agenda;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaLista {
	// Atributos consumidos en el formulario
	public String textoBuscar;

	// Instancia el objeto para acceso a datos.
	PersonaDAO personaDao = new PersonaDAO();

	// Objeto que contiene la persona y agenda con la que se esta trabajando
	private Persona personaSeleccionada;
	private Agenda agendaSeleccionada;
	private List<Persona> personas;

	/**
	 * Realiza la busqueda (actualiza la pantalla)
	 * 
	 * La anotacion "GlobalCommand" permite definir un comando que 
	 * puede ser ejecutado desde otro formulario u otro "hilo".
	 */
	@GlobalCommand("PersonaLista.buscar")
	@Command
	@NotifyChange({"personas", "personaSeleccionada.agendas", "personaSeleccionada", "agendaSeleccionada"})
	public void buscar(){

		if (personas != null) {
			personas = null; 
		}

		personas = personaDao.getPersonas(textoBuscar);

		// Limpia os objetos de trabajo
		personaSeleccionada = null; 
		agendaSeleccionada = null; 

	}
	
	/**
	 * Crea una persona
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", new Persona());
		Window ventanaCargar = (Window) Executions.createComponents("/mix/personaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		personaDao.getEntityManager().refresh(personaSeleccionada);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/mix/personaEditar.zul", null, params);
		ventanaCargar.doModal();
		
	}

	/**
	 * Borra "fisicamente" un registro
	 */
	@Command
	public void borrar(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}
		
		Messagebox.show("Desea borrar el registro seleccionado?", "Confirmación de borrado", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						personaDao.getEntityManager().getTransaction().begin();
						personaDao.getEntityManager().refresh(personaSeleccionada);
						personaDao.getEntityManager().remove(personaSeleccionada);
						personaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "PersonaLista.buscar", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}

	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){
		
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						personaDao.getEntityManager().getTransaction().begin();
						personaSeleccionada.setEstado("X");
						personaSeleccionada = personaDao.getEntityManager().merge(personaSeleccionada);
						personaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "PersonaLista.buscar", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}
	
	

	/**
	 * Limpia el objeto de agenda seleccionada
	 */
	@Command
	public void limpiaAgenda() {
		//System.out.println("Seleccionada" + personaSeleccionada.getNombre());
		agendaSeleccionada = null; 
	}

	/**
	 * Crea una nueva agenda
	 */
	@Command
	public void agendaNuevo(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		personaDao.getEntityManager().refresh(personaSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		params.put("Agenda", new Agenda());
		Window ventanaCargar = (Window) Executions.createComponents("/mix/personaAgendaEditar.zul", null, params);
		ventanaCargar.doModal();

	}

	/**
	 * Edita una agenda
	 */
	@Command
	public void agendaEditar(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}

		if (agendaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una agenda.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		personaDao.getEntityManager().refresh(personaSeleccionada);
		personaDao.getEntityManager().refresh(agendaSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		params.put("Agenda", agendaSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/mix/personaAgendaEditar.zul", null, params);
		ventanaCargar.doModal();

	}

	/**
	 * Borra logicamente una agenda
	 */
	@Command
	public void agendaBorrar(){

		if (agendaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una agenda.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						personaDao.getEntityManager().getTransaction().begin();
						agendaSeleccionada.setEstado("X");
						agendaSeleccionada = personaDao.getEntityManager().merge(agendaSeleccionada);
						personaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "PersonaLista.buscar", null);

					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}





	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	public Persona getPersonaSeleccionada() {
		return personaSeleccionada;
	}
	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
	}
	public Agenda getAgendaSeleccionada() {
		return agendaSeleccionada;
	}
	public void setAgendaSeleccionada(Agenda agendaSeleccionada) {
		this.agendaSeleccionada = agendaSeleccionada;
	}
	public List<Persona> getPersonas() {
		return personas;
	}
	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}
}
