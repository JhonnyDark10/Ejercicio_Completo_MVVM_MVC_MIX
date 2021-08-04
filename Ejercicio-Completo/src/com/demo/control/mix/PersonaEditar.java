package com.demo.control.mix;

import java.util.List;

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

import com.demo.modelo.Pai;
import com.demo.modelo.PaiDAO;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

public class PersonaEditar {
	@Wire
	private Window winPersonaEditar;
	
	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();
	private PaiDAO paisDao = new PaiDAO();
	
	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;
	
	/**
	 * Metodo que emula al metodo doAfterComposer de MVC
	 */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

	    // Recupera el objeto pasado como parametro. 
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");

	}

	
	/**
	 * Graba la informacion. Para poder cerrar la ventana, pasa como parametro
	 * desde el formulario la ventana: onClick="@command('grabar', ventana=winPersonaEditar)"
	 */
	@Command
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
	 * la ventana a cerrar: onClick="@command('salir', ventana=winPersonaEditar)"
	 */
	@Command
	public void salir(){
		winPersonaEditar.detach();
	}

	
	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */
	public List<Pai> getPaises() {
		return paisDao.getPaises();
	}
	

//Getters and Setters
	public Persona getPersona() {
		return persona;
	}


	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
}
