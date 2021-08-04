package com.demo.modelo;

import java.util.List;

import javax.persistence.Query;

public class PersonaDAO extends ClaseDAO{
	/**
	 * Busca una persona en base a su id.
	 * @param id
	 * @return
	 */
	public Persona getPersonaPorId(long id) {
		Persona persona;
		persona = (Persona) getEntityManager().find(Persona.class, id);
		return persona;
	}

	/**
	 * Busca personas en base a un patron de busqueda.
	 * @param value
	 * @return
	 */
	public List<Persona> getPersonas(String value) {
		List<Persona> resultado; 
		String patron = value;

		// Adapta el patron de busqueda.
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}

		// Crea la consulta.
		Query query = getEntityManager().createNamedQuery("Personas.buscarPorPatron");
		
		// Para asegurar que la consulta trae lo ultimo de la base.
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		
		// Asigna el patron de busqueda
		query.setParameter("patron", patron);
		
		// Recupera los resultados.
		resultado = (List<Persona>) query.getResultList();
		
		return resultado;
	}
}
