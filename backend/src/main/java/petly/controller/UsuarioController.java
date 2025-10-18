package petly.controller;

import jakarta.validation.Valid;
import petly.repository.UsuarioDAO;
// import org.repaso.dto.PedidoDTO;
import petly.model.Usuario;
import petly.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller

@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping()
    public String listarUsuarios(Model model) {
        List<Usuario> listaUsuarios = usuarioService.listAll();
        model.addAttribute("listaUsuarios", listaUsuarios);

        return "usuarios/usuarios";
    }

    @GetMapping("/{id}")
    public String detalle(Model model, @PathVariable int id) {
        Usuario usuario = usuarioService.one(id);
        model.addAttribute("usuario", usuario);

       // List<PedidoDTO> pedidosDTO = clienteService.listPedidosDTO(id);
       // model.addAttribute("pedidosDTO", pedidosDTO);

        return "usuarios/detalles";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);

        return "usuarios/crear";
    }

    @PostMapping("/crear")
    public String submitCrear(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResulted, Model model) {
        if (bindingResulted.hasErrors()) {
            model.addAttribute("usuario", usuario);

            return "usuarios/crear";
        }
        usuarioService.newUsuario(usuario);

        return "/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(Model model, @PathVariable Integer id) {

        Usuario usuario = usuarioService.one(id);
        model.addAttribute("usuario", usuario);

        return "usuarios/editar";

    }

    @PostMapping("/editar/{id}")
    public String submitEditar(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResulted, @PathVariable Integer id, Model model) {
        if (bindingResulted.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", id);

            return "/usuarios/editar";
        }
        usuarioService.updateUsuario(usuario);

        return "/usuarios";
    }

    @PostMapping("/borrar/{id}")
    public RedirectView borrar(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);

        return new RedirectView("/usuarios");
    }
}
