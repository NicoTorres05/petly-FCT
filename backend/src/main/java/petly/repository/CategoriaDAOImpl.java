package petly.repository;

import petly.model.Categoria;

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
public class CategoriaDAOImpl implements CategoriaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void create(Categoria categoria) {
        String sqlInsert = """
							INSERT INTO categoria (nombre, descripcion, imagen) 
							VALUES  (?, ?, ?)
						   """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id_usuario" });
            int idx = 1;
            ps.setString(idx++, categoria.getNombre());
            ps.setString(idx++, categoria.getDescripcion());
            ps.setString(idx, categoria.getImagen());
            return ps;
        },keyHolder);

        categoria.setId(keyHolder.getKey().intValue());

        log.info("Insertados {} registros.", rows);
    }

    @Override
    public void update(Categoria categoria) {
        String sqlUpdate = """
        UPDATE categoria 
        SET nombre = ?, 
            descripcion = ?, 
            imagen = ?
        WHERE id_categoria = ?
    """;

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUpdate);
            int idx = 1;
            ps.setString(idx++, categoria.getNombre());
            ps.setString(idx++, categoria.getDescripcion());
            ps.setString(idx++, categoria.getImagen());
            ps.setInt(idx, categoria.getId());
            return ps;
        });

        log.info("Actualizados {} registros en la tabla categoria.", rows);
    }

    @Override
    public void delete(Categoria categoria) {
        String sqlDelete = """
        DELETE FROM categoria
        WHERE id_categoria = ?
    """;

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlDelete);
            int idx = 1;
            ps.setInt(idx, categoria.getId());
            return ps;
        });

        log.info("Eliminados {} registros de la tabla categoria (id = {}).", rows, categoria.getId());
    }

    @Override
    public Categoria find(int id) {
        String sqlFind = """
        SELECT id_categoria, nombre, descripcion, imagen
        FROM categoria
        WHERE id_categoria = ?
    """;

        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFind);
            int idx = 1;
            ps.setInt(idx, id);
            return ps;
        }, (ResultSet rs) -> {
            Categoria categoria = null;
            if (rs.next()) {
                categoria = new Categoria();
                categoria.setId(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categoria.setImagen(rs.getString("imagen"));
            }

            if (categoria != null) {
                log.info("Categoría encontrada: {}", categoria.getNombre());
            } else {
                log.warn("No se encontró ninguna categoría con id {}", id);
            }

            return categoria;
        });
    }

    @Override
    public List<Categoria> findAll() {
        String sqlFindAll = """
        SELECT id_categoria, nombre, descripcion, imagen
        FROM categoria
    """;

        List<Categoria> categorias = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFindAll);
            return ps;
        }, (ResultSet rs) -> {
            List<Categoria> lista = new ArrayList<>();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categoria.setImagen(rs.getString("imagen"));
                lista.add(categoria);
            }
            return lista;
        });

        log.info("Se encontraron {} categorías en la base de datos.", categorias.size());
        return categorias;
    }
}
