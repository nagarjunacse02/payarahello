/*
 * Created by Desire Bah Bioh
 * copyright 2017
 * RESTful API running on payara micro
 */
package com.dez;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/hello")
public class HelloRessource {

    // Create an EntityManagerFactory when you start the application.
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.
            createEntityManagerFactory("dezdb");

    /**
     * Add a message
     *@param message string to add to the url as the message
     *@return Response
     * */
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGreeting(@QueryParam("message") String message) {
        //create entity manager
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        //create entity transaction
        EntityTransaction transaction = null;

        try {
            //get a transaction
            transaction = entityManager.getTransaction();
            //begin the transaction
            transaction.begin();
            //create a new message
            Message msg = new Message();
            msg.setMessage(message);
            //save message
            entityManager.persist(msg);
            //commit transaction
            transaction.commit();
            System.out.println("message successfuly created!");
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            //print the exception
            ex.printStackTrace();
        } finally {
            //close entity manager
            entityManager.close();
        }

        return Response.ok().build();
    }

    /**
     * Display all messages
     *@return Response
     * */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response DisplayMessage() {
        List<Message> messages = null;

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        //create entity transaction
        EntityTransaction transaction = null;

        try {
            //get a transaction
            transaction = entityManager.getTransaction();
            //begin the transaction
            transaction.begin();
            //get list of messages
            messages = entityManager.createQuery("select m from Message m", Message.class).getResultList();
            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the EntityManager
            entityManager.close();
        }
        return Response.ok(messages).build();
    }

    /**
     * Find a message by id
     *@param id passing message id to the url
     *@return Response
     * */
    @GET
    @Path("/message")
    public Response getMessage(@QueryParam("id") int id){
         Message message = new Message();
        //create entity manager
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        //create entity transaction
        EntityTransaction transaction = null;

        try {
            // Get a transaction
            transaction = entityManager.getTransaction();
            // Begin the transaction
            transaction.begin();
            Query query = entityManager.createQuery("select m from Message m where m.id = :id").setParameter("id",id);
            message = (Message)query.getSingleResult();
        }catch (NoResultException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the EntityManager
            entityManager.close();
        }

        return  Response.ok(message).build();
    }

    /**
     * Delete a message
     *@param id passing the message id to the url
     * */
    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteMsg(@QueryParam("id") int id) {
        //create entity manager
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        //create entity transaction
        EntityTransaction transaction = null;

        try {
            // Get a transaction
            transaction = entityManager.getTransaction();
            // Begin the transaction
            transaction.begin();

            // Get the Message object
            Message msg = entityManager.find(Message.class, id);
            if(msg == null){
              System.out.println("NOT FOUND!");
            }else {
                // Delete the message
                entityManager.remove(msg);
                // Commit the transaction
                transaction.commit();
                System.out.println("message succesfully deleted!!!");
            }
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the EntityManager
            entityManager.close();
        }
    }

    /**
     * Update a message
     *@param id passing the message id to the url
     *@param message passing the modified message to the url
     * */
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateMsg(@QueryParam("id") int id, @QueryParam("message") String message) {
        // Create an EntityManager
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            // Get a transaction
            transaction = entityManager.getTransaction();
            // Begin the transaction
            transaction.begin();
            // Get the Message object
            Message msg = entityManager.find(Message.class, id);
            // Change the value
            msg.setMessage(message);
            // Update the message
            entityManager.persist(msg);
            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the EntityManager
            entityManager.close();
        }
    }
}


