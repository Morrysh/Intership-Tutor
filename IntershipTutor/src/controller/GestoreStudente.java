package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.StudenteDAO;
import data.model.Studente;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;
import framework.result.FailureResult;
import framework.result.TemplateManagerException;
import framework.security.SecurityLayer;

@SuppressWarnings("serial")
public class GestoreStudente extends IntershipTutorBaseController {
	
	public static final String SERVLET_URI = "/studente";

	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
	
	private void action_aggiorna(HttpServletRequest request, HttpServletResponse response) {
		try {
			Studente studente = (Studente) request.getAttribute("utente");
			
			studente.setNome(request.getParameter(Studente.NOME));
			studente.setCognome(request.getParameter(Studente.COGNOME));
			studente.setEmail(request.getParameter(Utente.EMAIL));
			studente.setUsername(request.getParameter(Utente.USERNAME));
			studente.setPassword(request.getParameter(Utente.PASSWORD));
			studente.setTelefono(request.getParameter(Utente.TELEFONO));
			studente.setDataNascita(SecurityLayer.checkDate(request.getParameter(Studente.DATA_NASCITA)));
			studente.setLuogoNascita(request.getParameter(Studente.LUOGO_NASCITA));
			studente.setProvinciaNascita(request.getParameter(Studente.PROVINCIA_NASCITA));
			studente.setResidenza(request.getParameter(Studente.RESIDENZA));
			studente.setProvinciaResidenza(request.getParameter(Studente.PROVINCIA_RESIDENZA));
			studente.setTipoLaurea(request.getParameter(Studente.TIPO_LAUREA));
			studente.setCorsoLaurea(request.getParameter(Studente.CORSO_LAUREA));
			studente.setHandicap(SecurityLayer.checkBoolean(request.getParameter(Studente.HANDICAP)));
			
			new StudenteDAO().update(studente);
			
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
		}
		catch(DataLayerException | IOException | IllegalArgumentException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}
	
	private void action_registra(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException{
		try {
			Studente studente = new Studente(
				request.getParameter(Utente.CODICE_FISCALE),
				request.getParameter(Utente.EMAIL),
				request.getParameter(Utente.USERNAME),
				request.getParameter(Utente.PASSWORD),
				request.getParameter(Utente.TELEFONO),
				TipoUtente.studente,
				request.getParameter(Studente.CODICE_FISCALE),
				request.getParameter(Studente.NOME),
				request.getParameter(Studente.COGNOME),
				SecurityLayer.checkDate(request.getParameter(Studente.DATA_NASCITA)),
				request.getParameter(Studente.LUOGO_NASCITA),
				request.getParameter(Studente.PROVINCIA_NASCITA),
				request.getParameter(Studente.RESIDENZA),
				request.getParameter(Studente.PROVINCIA_RESIDENZA),
				request.getParameter(Studente.TIPO_LAUREA),
				request.getParameter(Studente.CORSO_LAUREA),
				SecurityLayer.checkBoolean(request.getParameter(Studente.HANDICAP)));
			
			new StudenteDAO().insert(studente);
			
			// NOT USING request.getContextPath becouse it doesn't work with Heroku
			response.sendRedirect(".");
		}
		catch(DataLayerException | IOException | IllegalArgumentException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
	}

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

    	try {
			if(request.getParameter("aggiorna") != null) {
				// Verifichiamo che ci sia un utente loggato
				// Verifichiamo che l'utente loggato sia uno studente
				// Verifichiamo che lo studente stia effettivamente modificando il suo profilo
				if(request.getAttribute("utente") != null &&
				   request.getAttribute("utente") instanceof Studente &&
				   ((Studente)request.getAttribute("utente")).getCodiceFiscale().equals(request.getParameter("utente"))) {
					action_aggiorna(request, response);
				}
			}
			else if(request.getParameter("registrazione") != null) {
				action_registra(request, response);
			}
        }
    	catch (TemplateManagerException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        }
    }

    @Override
    public String getServletInfo() {
        return "Student details";
    }

}
