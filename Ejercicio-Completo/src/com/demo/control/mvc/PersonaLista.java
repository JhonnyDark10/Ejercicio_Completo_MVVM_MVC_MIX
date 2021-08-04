package com.demo.control.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.demo.modelo.Agenda;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaLista extends SelectorComposer {
	@Wire
	private Window winListaPersonas;

	@Wire
	private Textbox txtBuscar;

	@Wire 
	private Listbox lstPersonas, lstAgendas;

	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();

	private List<Persona> personas;
	private Persona personaSeleccionada;
	private Agenda agendaSeleccionada;

	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){

		if (personas != null) {
			personas = null; 
		}

		personas = personaDao.getPersonas(txtBuscar.getValue());

		lstPersonas.setModel(new ListModelList(personas));

		// Limpia os objetos de trabajo
		personaSeleccionada = null; 
		agendaSeleccionada = null;

		// Actualiza la lista de agendas
		AnnotateDataBinder binder = new AnnotateDataBinder(winListaPersonas);
		binder.loadComponent(lstAgendas);

	}

	/**
	 * Escucha el evento "onClick" del objeto "btnNuevo"
	 * Carga el formulario sin una persona para crear uno nuevo.
	 */
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/mvc/personaEditar.zul", winListaPersonas, params);
		ventanaCargar.doModal();
	}

	/**
	 * Escucha el evento "onClick" del objeto "btnEditar"
	 * Carga el formulario pasando la persona a editar.
	 */
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		personaDao.getEntityManager().refresh(personaSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/mvc/personaEditar.zul", winListaPersonas, params);
		ventanaCargar.doModal();

	}

	/**
	 * Escucha el evento "onClick" del objeto "btnBorrar"
	 * Borra permanentemente una persona 
	 */
	@Listen("onClick=#btnBorrar")
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
						buscar();
					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}

	/**
	 * Escucha el evento "onClick" del objeto "btnEliminar"
	 * Elimina logicamente una persona.
	 */
	@Listen("onClick=#btnEliminar")
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
						personaSeleccionada.setEstado("X");
						personaDao.getEntityManager().getTransaction().begin();
						personaDao.getEntityManager().persist(personaSeleccionada);
						personaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						buscar();
					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}
	
	/**
	 * Escucha el evento "onClick" del objeto "btnAgendaNuevo"
	 * Carga el formulario pasando la persona a editar.
	 */
	@Listen("onClick=#btnAgendaNuevo")
	public void agendaNuevo(){
		if (personaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		personaDao.getEntityManager().refresh(personaSeleccionada);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		params.put("Agenda", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/mvc/personaAgendaEditar.zul", winListaPersonas, params);
		ventanaCargar.doModal();
		
	}

	/**
	 * Escucha el evento "onClick" del objeto "btnAgendaEditar"
	 * Carga el formulario pasando la persona a editar.
	 */
	@Listen("onClick=#btnAgendaEditar")
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
		//personaDao.getEntityManager().refresh(personaSeleccionada);
		//personaDao.getEntityManager().refresh(agendaSeleccionada);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", personaSeleccionada);
		params.put("Agenda", agendaSeleccionada);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/mvc/personaAgendaEditar.zul", winListaPersonas, params);
		ventanaCargar.doModal();
		
	}

	/**
	 * Escucha el evento "onClick" del objeto "btnAgendaBorrar"
	 * Carga el formulario pasando la persona a editar.
	 */
	@Listen("onClick=#btnAgendaBorrar")
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
						agendaSeleccionada.setEstado("X");
						personaDao.getEntityManager().getTransaction().begin();
						personaDao.getEntityManager().persist(agendaSeleccionada);
						personaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						buscar();
					} catch (Exception e) {
						e.printStackTrace();
						personaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	
	//Getters and Setters
	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
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
	
	
	

}
