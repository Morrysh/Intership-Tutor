package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.StudenteDAOInterface;
import database.DBConnector;
import model.Studente;
import model.Utente;

public class StudenteDAO implements StudenteDAOInterface {

	@Override
	public int insert(Studente studente) {
		String insertQuery = "INSERT INTO studente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, studente.getNome());
            preparedStatement.setString(2, studente.getCognome());
            preparedStatement.setDate(3, studente.getDataNascita());
            preparedStatement.setString(4, studente.getLuogoNascita());
            preparedStatement.setString(5, studente.getResidenza());
            preparedStatement.setString(6, studente.getProvincia());
            preparedStatement.setString(7, studente.getTipoLaurea());
            preparedStatement.setString(8, studente.getCorsoLaurea());
            preparedStatement.setBoolean(9, studente.isHandicap());
            preparedStatement.setString(10, studente.getUtente());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(Studente studente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Studente studente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Studente getStudenteByUtente(Utente utente) {
		String query = "SELECT * FROM studente WHERE id = ?;";
        PreparedStatement preparedStatement;
        Studente studente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, utente.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                studente = new Studente(
                		resultSet.getString(Studente.UTENTE),
						resultSet.getString(Studente.NOME),
                        resultSet.getString(Studente.COGNOME),
                        resultSet.getDate(Studente.DATA_NASCITA),
                        resultSet.getString(Studente.LUOGO_NASCITA),
                        resultSet.getString(Studente.RESIDENZA),
                        resultSet.getString(Studente.PROVINCIA),
                        resultSet.getString(Studente.TIPO_LAUREA),
                        resultSet.getString(Studente.CORSO_LAUREA),
                        resultSet.getBoolean(Studente.HANDICAP));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studente;
	}

}
