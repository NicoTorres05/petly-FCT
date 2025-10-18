package petly.repository;

import petly.model.Categoria;
import petly.model.Producto;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProductoDAOImpl implements ProductoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void create(Producto producto) {
        String sqlInsert = """
        INSERT INTO producto (nombre, descripcion, foto, categoria_id, precio, stock, descuento, fecha_creacion, activo)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id_producto" });
            int idx = 1;
            ps.setString(idx++, producto.getNombre());
            ps.setString(idx++, producto.getDescripcion());
            ps.setString(idx++, producto.getFoto());
            ps.setInt(idx++, producto.getCategoria().getId());
            ps.setBigDecimal(idx++, producto.getPrecio());
            ps.setObject(idx++, producto.getStock(), java.sql.Types.INTEGER);
            ps.setInt(idx++, producto.getDescuento());
            ps.setTimestamp(idx++, Timestamp.valueOf(producto.getFechaCreacion()));
            ps.setBoolean(idx, producto.getActivo());
            return ps;
        }, keyHolder);

        producto.setId(keyHolder.getKey().intValue());

        log.info("Insertados {} registros en la tabla producto.", rows);
    }

    @Override
    public void update(Producto producto) {
        String sqlUpdate = """
        UPDATE producto
        SET nombre = ?,
            descripcion = ?,
            foto = ?,
            categoria_id = ?,
            precio = ?,
            stock = ?,
            descuento = ?,
            fecha_creacion = ?,
            activo = ?
        WHERE id_producto = ?
    """;

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUpdate);
            int idx = 1;
            ps.setString(idx++, producto.getNombre());
            ps.setString(idx++, producto.getDescripcion());
            ps.setString(idx++, producto.getFoto());
            ps.setInt(idx++, producto.getCategoria().getId());
            ps.setBigDecimal(idx++, producto.getPrecio());
            ps.setObject(idx++, producto.getStock(), java.sql.Types.INTEGER);
            ps.setInt(idx++, producto.getDescuento());
            ps.setTimestamp(idx++, Timestamp.valueOf(producto.getFechaCreacion()));
            ps.setBoolean(idx++, producto.getActivo());
            ps.setInt(idx, producto.getId());
            return ps;
        });

        log.info("Actualizados {} registros en la tabla producto (id = {}).", rows, producto.getId());
    }

    @Override
    public void delete(Producto producto) {
        String sqlDelete = """
        DELETE FROM producto
        WHERE id_producto = ?
    """;

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlDelete);
            ps.setInt(1, producto.getId());
            return ps;
        });

        log.info("Eliminados {} registros de la tabla producto (id = {}).", rows, producto.getId());
    }

    @Override
    public Producto find(int id) {
        String sqlFind = """
        SELECT id_producto, nombre, descripcion, foto, categoria_id, precio, stock, descuento, fecha_creacion, activo
        FROM producto
        WHERE id_producto = ?
    """;

        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFind);
            ps.setInt(1, id);
            return ps;
        }, (ResultSet rs) -> {
            Producto producto = null;
            if (rs.next()) {
                producto = new Producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setFoto(rs.getString("foto"));


                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                producto.setCategoria(categoria);

                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getObject("stock", Integer.class));
                producto.setDescuento(rs.getInt("descuento"));
                producto.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                producto.setActivo(rs.getBoolean("activo"));
            }

            if (producto != null) {
                log.info("Producto encontrado: {}", producto.getNombre());
            } else {
                log.warn("No se encontró ningún producto con id {}", id);
            }

            return producto;
        });
    }

    @Override
    public List<Producto> findAll() {
        String sqlFindAll = """
        SELECT id_producto, nombre, descripcion, foto, categoria_id, precio, stock, descuento, fecha_creacion, activo
        FROM producto
    """;

        List<Producto> productos = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlFindAll);
            return ps;
        }, (ResultSet rs) -> {
            List<Producto> lista = new ArrayList<>();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setFoto(rs.getString("foto"));

                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                producto.setCategoria(categoria);

                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getObject("stock", Integer.class));
                producto.setDescuento(rs.getInt("descuento"));
                producto.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                producto.setActivo(rs.getBoolean("activo"));

                lista.add(producto);
            }
            return lista;
        });

        log.info("Se encontraron {} productos en la base de datos.", productos.size());
        return productos;
    }
}
