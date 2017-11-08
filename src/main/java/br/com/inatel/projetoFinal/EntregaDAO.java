package br.com.inatel.projetoFinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
	public Connection openConection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/dm107finalprojectdb";
			String username = "root";
			String password = "root";

			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("O driver expecificado nao foi encontrado.");
		} catch (SQLException e) {
			System.out.println("Nao foi possivel conectar ao Banco de Dados.");
		}

		return connection;
	}

	public void closeConection(Connection connection) throws SQLException {
		connection.close();
	}

	public boolean criaEntrega(Entrega en) throws SQLException {
		boolean ret = false;

		if (en.getNumeroPedido() != null || en.getIdCliente() != null) {
			Connection conn = openConection();

			String sql = "INSERT INTO entregas (NumeroDoPedido, IdCliente, NomeRecebedor, CPFRecebedor, DataHora) VALUES (?,?,?,?,?);";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, en.getNumeroPedido());
			stmt.setInt(2, en.getIdCliente());
			stmt.setString(
					3,
					(en.getNomeRecebedor() == null ? "" : en.getNomeRecebedor()));
			stmt.setString(4,
					(en.getCpfRecebedor() == null ? "" : en.getCpfRecebedor()));
			stmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));

			stmt.executeUpdate();

			closeConection(conn);
		}
		return ret;
	}

	public Entrega buscaEntrega(int numeroPedido) throws SQLException {
		Entrega entrega = null;

		Connection conn = openConection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM entregas WHERE NumeroDoPedido = " + numeroPedido + ";");
		System.out.println("SELECT * FROM entregas WHERE NumeroDoPedido = " + numeroPedido + ";");
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			entrega = new Entrega();
			
			entrega.setId(rs.getInt("Id"));
			entrega.setNumeroPedido(rs.getInt("NumeroDoPedido"));
			entrega.setIdCliente(rs.getInt("IdCliente"));
			entrega.setNomeRecebedor(rs.getString("NomeRecebedor"));
			entrega.setCpfRecebedor(rs.getString("CPFRecebedor"));
			entrega.setDataHoraEntrega(rs.getDate("DataHora"));
		}

		stmt.close();
		closeConection(conn);
		
		return entrega;
	}
	
	public boolean validaCliente(String nome, String senha) throws SQLException {
		boolean ret = false;
		Connection conn = openConection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clientes WHERE Nome = '" + nome + "' and Senha = '" + senha + "';");
		System.out.println("SELECT * FROM clientes WHERE Nome = '" + nome + "' and Senha = '" + senha + "';");
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			ret = true;
		}

		stmt.close();
		closeConection(conn);
		
		return ret;
	}
}
