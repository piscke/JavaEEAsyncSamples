/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.dao;

import piscke.business.vo.Funcionario;
import piscke.business.vo.Funcionario_;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author leandro.piscke
 */
@Stateless
@LocalBean
public class FuncionarioDAO {

    @PersistenceContext
    EntityManager em;

    public Funcionario buscaPorId(long id) {
        try {
            return em.find(Funcionario.class, id);
        } catch (NoResultException err) {
            return null;
        }
    }

    public Funcionario buscaPorNome(String nome) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
        Root<Funcionario> funcionario = cq.from(Funcionario.class);

        cq.where(cb.equal(funcionario.get(Funcionario_.nome), nome));
        TypedQuery<Funcionario> query = em.createQuery(cq);

        try {
            return query.getSingleResult();
        } catch (NoResultException err) {
            return null;
        }
    }

    public void persistir(Funcionario funcionario) {
        em.persist(funcionario);
        System.out.println(String.format("persistido: %s - %s", funcionario.getNome(), funcionario.getId()));
    }

    public void cancelar() {
        em.clear();
        System.out.println("cancelado");
    }
    
    public void merge(Funcionario funcionario) {
        em.merge(funcionario);
        System.out.println(String.format("persistido: %s - %s", funcionario.getNome(), funcionario.getId()));
    }

    public List<Funcionario> buscaAtivos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
        Root<Funcionario> funcionario = cq.from(Funcionario.class);

        cq.where(cb.equal(funcionario.get(Funcionario_.ativo), true));
        TypedQuery<Funcionario> query = em.createQuery(cq);

        try {
            return query.getResultList();
        } catch (NoResultException err) {
            return null;
        }
    }
    
    public long contagem() {
        Query query = em.createQuery("select count(e) from " + Funcionario.class.getSimpleName() +" e");  

        try {
            return (long) query.getSingleResult();
        } catch (NoResultException err) {
            return 0;
        }
    }    
    
    public List<Funcionario> buscaPaginado(int primeiroItem, int numeroItens) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
        TypedQuery<Funcionario> tq = em.createQuery(cq);
        tq.setFirstResult(primeiroItem);
        tq.setMaxResults(numeroItens);

        return tq.getResultList();
    }

    public List<Funcionario> buscaTodos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
        TypedQuery<Funcionario> tq = em.createQuery(cq);

        return tq.getResultList();
    }

    public void inserirDados() {

        String[] nomes = {"Leandro", "Paulo", "Rafael", "Camila", "João", "Carlos", "Gabriel", "Wendy", "Henrrique", "Diego"};

        for (String nome : nomes) {

            Funcionario funcionario = this.buscaPorNome(nome);

            if (funcionario == null) {
                Funcionario func = new Funcionario();
                func.setAtivo(true);
                func.setNome(nome);
                this.persistir(func);
            } else {
                funcionario.setAtivo(true);
            }
        }
    }
}
