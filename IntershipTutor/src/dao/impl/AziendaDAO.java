package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.AziendaDAOInterface;
import database.DBConnector;
import model.Azienda;
import model.Utente;

public class AziendaDAO implements AziendaDAOInterface {

	@Override
	public int insert(Azienda azienda) {
		String insertQuery = "INSERT INTO azienda VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connectionection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connectionection.prepareStatement(insertQuery);

            preparedStatement.setString(1, azienda.getNome());
            preparedStatement.setString(2, azienda.getRegione());
            preparedStatement.setString(3, azienda.getIndirizzoSedeLegale());
            preparedStatement.setString(4, azienda.getForoCompetente());
            preparedStatement.setBoolean(5, azienda.getConvenzionata());
            preparedStatement.setString(6, azienda.getNomeRappresentante());
            preparedStatement.setString(7, azienda.getCognomeRappresentante());
            preparedStatement.setString(8, azienda.getNomeResponsabile());
            preparedStatement.setString(9, azienda.getCognomeResponsabile());
            preparedStatement.setString(10, azienda.getUtente());

            status = preparedStatement.executeUpdate();

            connectionection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(Azienda azienda) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setConvenzione(Azienda azienda, boolean convezione) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Azienda getAziendaByCodiceFiscale(String codiceFiscale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Azienda> allAziende() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Azienda> allAziendeAccordingToConvention(boolean convenzione) {
		List<Azienda> aziende = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM azienda WHERE convenzionata = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, convenzione ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Azienda azienda = new Azienda(
                		resultSet.getString(Azienda.NOME),
						resultSet.getString(Azienda.REGIONE),
						resultSet.getString(Azienda.INDIRIZZO_SEDE_LEGALE),
						resultSet.getString(Azienda.FORO_COMPETENTE),
	                    resultSet.getString(Azienda.NOME_RAPPRESENTANTE),
	                    resultSet.getString(Azienda.COGNOME_RAPPRESENTANTE),
	                    resultSet.getString(Azienda.NOME_RESPONSABILE),
	                    resultSet.getString(Azienda.COGNOME_RESPONSABILE),
	                    resultSet.getString(Azienda.UTENTE),
	                    resultSet.getBoolean(Azienda.CONVENZIONATA));
            aziende.add(azienda);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return aziende;
	}

	@Override
	public Azienda getAziendaByUtente(Utente utente) {
		 String query = "SELECT * FROM azienda WHERE id = ?;";
	        PreparedStatement preparedStatement;
	        Azienda azienda = null;

	        try (Connection connection = DBConnector.getDatasource().getConnection()) {
	            preparedStatement = connection.prepareStatement(query);

	            preparedStatement.setString(1, utente.getCodiceFiscale());

	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                azienda = new Azienda(
	                		resultSet.getString(Azienda.NOME),
	                        resultSet.getString(Azienda.REGIONE),
	                        resultSet.getString(Azienda.INDIRIZZO_SEDE_LEGALE),
	                        resultSet.getString(Azienda.FORO_COMPETENTE),
	                        resultSet.getString(Azienda.NOME_RAPPRESENTANTE),
	                        resultSet.getString(Azienda.COGNOME_RAPPRESENTANTE),
	                        resultSet.getString(Azienda.NOME_RESPONSABILE),
	                        resultSet.getString(Azienda.COGNOME_RESPONSABILE),
	                        resultSet.getString(Azienda.UTENTE),
	                        resultSet.getBoolean(Azienda.CONVENZIONATA));
	            }

	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return azienda;
	}
	
	
}
