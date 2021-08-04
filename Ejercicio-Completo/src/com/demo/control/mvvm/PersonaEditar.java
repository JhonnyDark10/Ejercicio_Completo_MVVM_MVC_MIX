package com.demo.control.mvvm;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Window;

import com.demo.modelo.Pai;
import com.demo.modelo.PaiDAO;
import com.demo.modelo.Persona;
import com.demo.modelo.PersonaDAO;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * Clase de control del formulario personaEditar.zul
 * @author Luis
 *
 */
public class PersonaEditar {
	
	// Instancia el objeto para acceso a datos.
	private PersonaDAO personaDao = new PersonaDAO();
	private PaiDAO paisDao = new PaiDAO();
	
	// Objeto que contiene la persona con la que se esta trabajando
	private Persona persona;
	
	/**
	 * Constructor para obtener el parametro enviado.
	 */
	public PersonaEditar() {
		
	    // Recupera el objeto pasado como parametro. 
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
				
	}

	
	/**
	 * Graba la informacion. Para poder cerrar la ventana, pasa como parametro
	 * desde el formulario la ventana: onClick="@command('grabar', ventana=winPersonaEditar)"
	 */
	@Command
	public void grabar(@BindingParam("ventana")  Window ventana){

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
			salir(ventana);
			
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
	public void salir(@BindingParam("ventana")  Window ventana){
		ventana.detach();
	}

	
	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */
	public List<Pai> getPaises() {
		return paisDao.getPaises();
	}

	// Validadores
	
	/**
	 * Validador personalizado
	 * @return
	 */
	public Constraint getValidaFecha() {
		return new Constraint() {
			
			@Override
			public void validate(Component comp, Object value) throws WrongValueException {
				Date valor = null; 
				if (value == null) {
					throw new WrongValueException("No se aceptan fechas vacías");
				}
				valor = (Date) value; 
				if (valor.after(new Date())) {
					throw new WrongValueException("¡En esa fecha aun no ha nacido!");
				}
			}
		};		
	}


	public Persona getPersona() {
		return persona;
	}


	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	//Getters and Setters
	
	
	
}
