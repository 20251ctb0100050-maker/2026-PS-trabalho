import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    public static Connection conectar() throws SQLException {

        ConfiguracaoBanco config = ConfiguracaoBanco.getInstancia();

        String usuario = config.getUsuario();
        String senha = config.getSenha();

        if (usuario == null || senha == null) {
            throw new SQLException("Configure DB_USER e DB_PASSWORD.");
        }

        return DriverManager.getConnection(
            config.getUrl(), usuario, senha
        );
    }
}
