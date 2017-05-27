package com.luizalabs.cache.controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.luizalabs.spring.model.Partida;

/*
 * Classe de controle para funcoes que guardam os dados de partidas
 * 
 */
public class PartidaController {

	/*
	 * Salva a partida
	 */
	public static void salvarPartida(Partida partida) {
		// Configure the session factory
		HibernateController.configureSessionFactory();

		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateController.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// Check if records already exists. If yes just update
			Criteria criteria = session.createCriteria(Partida.class);
			criteria.add(Restrictions.eq("id", partida.getId()));
			criteria.setProjection(Projections.rowCount());
			long count = (Long) criteria.uniqueResult();

			// Saving to the database
			if (count == 0)
				session.save(partida);
			else
				session.update(partida);

			// Committing the change in the database.
			tx.commit();
			session.flush();

		} catch (Exception ex) {
			ex.printStackTrace();

			// Rolling back the changes to make the data consistent in case of
			// any failure in between multiple database write operations.
			tx.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * Remove uma partida
	 */
	public static void removePartida(Partida partida) {
		// Configure the session factory
		HibernateController.configureSessionFactory();

		Session session = null;
		try {
			session = HibernateController.getSessionFactory().openSession();

			Query q = session.createQuery("delete Partida where id = " + partida.getId());
			q.executeUpdate();

			session.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * lista todas as partidas
	 */
	@SuppressWarnings("unchecked")
	public static List<Partida> getPartida() {
		// Configure the session factory
		HibernateController.configureSessionFactory();

		Session session = null;
		List<Partida> partidaList = null;
		try {
			session = HibernateController.getSessionFactory().openSession();

			// Fetching saved data
			partidaList = session.createQuery("from Partida").list();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return partidaList;
	}

	/*
	 * busca uma partida pelo id
	 */
	@SuppressWarnings("unchecked")
	public static List<Partida> getPartida(int id) {
		// Configure the session factory
		HibernateController.configureSessionFactory();

		Session session = null;
		List<Partida> partidaList = null;
		try {
			session = HibernateController.getSessionFactory().openSession();
			partidaList = session.createQuery("from Partida where id = " + id).list();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return partidaList;
	}
}
