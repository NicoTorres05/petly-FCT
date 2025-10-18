package petly.repository;

import petly.model.Usuario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import petly.model.UsuarioTipo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public synchronized  void create(Usuario usuario) {
        String sqlInsert = """
							INSERT INTO usuario (nombre, email, contrasena, direccion, tipo, saldo, pfp, fecha_registro) 
							VALUES  (?, ?, ?, ?, ?, ?, ?, ?)
						   """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id_usuario" });
            int idx = 1;
            ps.setString(idx++, usuario.getNombre());
            ps.setString(idx++, usuario.getEmail());
            ps.setString(idx++, usuario.getContrasena());
            ps.setString(idx++, usuario.getDireccion());
            ps.setString(idx, String.valueOf(usuario.getTipo()));
            ps.setBigDecimal(idx++, usuario.getSaldo());
            ps.setString(idx++, usuario.getPfp());
            ps.setDate(idx, Date.valueOf(usuario.getFechaRegistro()));
            return ps;
        },keyHolder);

        usuario.setId(keyHolder.getKey().intValue());

        log.info("Insertados {} registros.", rows);

    }

    @Override
    public void update(Usuario usuario) {
        String sqlUpdate = """ 
    UPDATE usuario SET nombre = ?,
                       email = ?,
                       contrasena = ?,
                       direccion = ?,
                       tipo = ?,
                       saldo = ?,
                       pfp = ?
                   WHERE id_usuario = ?
						   """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUpdate);
            int idx = 1;
            ps.setString(idx++, usuario.getNombre());
            ps.setString(idx++, usuario.getEmail());
            ps.setString(idx++, usuario.getContrasena());
            ps.setString(idx++, usuario.getDireccion());
            ps.setString(idx++, String.valueOf(usuario.getTipo()));
            ps.setBigDecimal(idx++, usuario.getSaldo());
            ps.setString(idx++, usuario.getPfp());
            ps.setDate(idx++, Date.valueOf(usuario.getFechaRegistro()));  // o ps.setTimestamp si es LocalDateTime
            ps.setInt(idx, usuario.getId());
            return ps;
        });

        log.info("Insertados {} registros.", rows);
    }

    @Override
    public synchronized void delete(Usuario usuario) {
        String sqlDelete = """
        DELETE FROM usuario
        WHERE id_usuario = ?
    """;

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlDelete);
            ps.setInt(1, usuario.getId());
            return ps;
        });

        log.info("Eliminados {} registros del usuario con ID {}", rows, usuario.getId());
    }


    @Override
    public synchronized Usuario find(int id) {
        String sqlFind = """
        SELECT id_usuario, nombre, email, contrasena, direccion, tipo, saldo, pfp, fecha_registro
        FROM usuario
        WHERE id_usuario = ?
    """;

        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFind);
            ps.setInt(1, id);
            return ps;
        }, (ResultSet rs) -> {
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setTipo(Enum.valueOf(UsuarioTipo.class, rs.getString("tipo"))); // si usas un enum
                usuario.setSaldo(rs.getBigDecimal("saldo"));
                usuario.setPfp(rs.getString("pfp"));
                usuario.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate()); // si es LocalDate
                return usuario;
            } else {
                return null;
            }
        });
    }


    @Override
    public synchronized List<Usuario> findAll() {
        String sqlFindAll = """
        SELECT id_usuario, nombre, email, contrasena, direccion, tipo, saldo, pfp, fecha_registro
        FROM usuario
    """;

        List<Usuario> usuarios = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFindAll);
            return ps;
        }, (ResultSet rs) -> {
            List<Usuario> lista = new ArrayList<>();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setTipo(Enum.valueOf(UsuarioTipo.class, rs.getString("tipo")));
                usuario.setSaldo(rs.getBigDecimal("saldo"));
                usuario.setPfp(rs.getString("pfp"));
                usuario.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
                lista.add(usuario);
            }
            return lista;
        });

        log.info("Se encontraron {} usuarios en la base de datos.", usuarios.size());
        return usuarios;
    }

}
