package com.demo.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PaiDAO extends ClaseDAO{
	/**
	 * Retorna la lista de opciones disponibles.
	 * @return
	 */
	public List<Pai> getPaises() {
		List<Pai> retorno = new ArrayList<Pai>();

		Query query = getEntityManager().createNamedQuery("Pai.findAll");
		retorno = (List<Pai>) query.getResultList();

		return retorno;

	}
}
