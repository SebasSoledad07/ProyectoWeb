package com.ufps.proyectoweb.controllers;

import com.ufps.proyectoweb.services.UserService;
import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import com.ufps.proyectoweb.enums.WeekDay;
import com.ufps.proyectoweb.models.IntakeQuestionnaire;
import com.ufps.proyectoweb.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/users/*")
public class UserController extends HttpServlet {

	private UserService userService;

	@Override
	public void init() throws ServletException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		if (context == null) {
			throw new ServletException("No se pudo obtener el contexto de Spring");
		}
		this.userService = context.getBean(UserService.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = normalizePath(req.getPathInfo());
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		switch (path) {
			case "/register" -> writeRegisterPage(req, resp);
			case "/login" -> writeLoginPage(req, resp);
			case "/health" -> writeHealthPage(req, resp);
			case "/home" -> writeHomePage(req, resp);
			case "/logout" -> logout(req, resp);
			default -> resp.sendRedirect(req.getContextPath() + "/users/login");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = normalizePath(req.getPathInfo());
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		switch (path) {
			case "/register" -> registerUser(req, resp);
			case "/login" -> loginUser(req, resp);
			case "/health" -> saveHealthForm(req, resp);
			default -> resp.sendRedirect(req.getContextPath() + "/users/login");
		}
	}

	private void registerUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user = buildUserFromRequest(req);

		if (isMissingRegistrationData(user)) {
			sendFlash(req.getSession(), "error", "Completa todos los campos del registro.");
			resp.sendRedirect(req.getContextPath() + "/users/register");
			return;
		}

		if (userService.emailExists(user.getEmail())) {
			sendFlash(req.getSession(), "error", "Ya existe un usuario con ese correo.");
			resp.sendRedirect(req.getContextPath() + "/users/register");
			return;
		}

		userService.register(user);
		HttpSession session = req.getSession(true);
		session.setAttribute("currentUser", user);
		sendFlash(session, "success", "Registro exitoso. Completa tu formulario de salud.");
		resp.sendRedirect(req.getContextPath() + "/users/health");
	}

	private void loginUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String email = safeParam(req, "email");
		String password = safeParam(req, "password");
		Role role = parseRole(req.getParameter("role"));

		if (email.isBlank() || password.isBlank() || role == null) {
			sendFlash(req.getSession(), "error", "Ingresa correo, contraseña y rol.");
			resp.sendRedirect(req.getContextPath() + "/users/login");
			return;
		}

		User user = userService.authenticate(email, password, role);
		if (user == null) {
			sendFlash(req.getSession(), "error", "Credenciales inválidas o rol incorrecto.");
			resp.sendRedirect(req.getContextPath() + "/users/login");
			return;
		}

		HttpSession session = req.getSession(true);
		session.setAttribute("currentUser", user);
		sendFlash(session, "success", "Bienvenido, " + user.getFirstName() + ".");
		resp.sendRedirect(req.getContextPath() + "/users/home");
	}

	private void saveHealthForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User currentUser = currentUser(req);
		if (currentUser == null) {
			sendFlash(req.getSession(), "error", "Debes iniciar sesión primero.");
			resp.sendRedirect(req.getContextPath() + "/users/login");
			return;
		}

		IntakeQuestionnaire questionnaire = buildQuestionnaireFromRequest(req);
		if (isMissingHealthData(questionnaire)) {
			sendFlash(req.getSession(), "error", "Completa todos los campos del formulario de salud.");
			resp.sendRedirect(req.getContextPath() + "/users/health");
			return;
		}

		currentUser.getMedicalQuestionnaires().add(questionnaire);
		userService.saveHealthForm(currentUser);
		HttpSession session = req.getSession(true);
		session.setAttribute("currentUser", currentUser);
		sendFlash(session, "success", "Formulario de salud guardado correctamente.");
		resp.sendRedirect(req.getContextPath() + "/users/home");
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		resp.sendRedirect(req.getContextPath() + "/users/login");
	}

	private void writeRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User currentUser = currentUser(req);
		writePage(resp, "Registro de usuario", pageHeader(req, "Registro de usuario") + flashMessage(req) +
				"<form method='post' action='" + req.getContextPath() + "/users/register'>" +
				input("Nombre", "firstName", "text", "") +
				input("Apellido", "lastName", "text", "") +
				input("Correo electrónico", "email", "email", "") +
				input("Contraseña", "password", "password", "") +
				input("Teléfono", "phoneNumber", "text", "") +
				select("Género", "gender", gendersOptions()) +
				input("Fecha de nacimiento", "birthDate", "date", "") +
				select("Rol", "role", rolesOptions()) +
				button("Registrarme") +
				"</form>" +
				footerLinks(req, currentUser));
	}

	private void writeLoginPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User currentUser = currentUser(req);
		writePage(resp, "Inicio de sesión", pageHeader(req, "Inicio de sesión") + flashMessage(req) +
				"<form method='post' action='" + req.getContextPath() + "/users/login'>" +
				input("Correo electrónico", "email", "email", "") +
				input("Contraseña", "password", "password", "") +
				select("Rol", "role", rolesOptions()) +
				button("Ingresar") +
				"</form>" +
				footerLinks(req, currentUser));
	}

	private void writeHealthPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User currentUser = currentUser(req);
		if (currentUser == null) {
			sendFlash(req.getSession(), "error", "Debes iniciar sesión para completar el formulario de salud.");
			resp.sendRedirect(req.getContextPath() + "/users/login");
			return;
		}

		writePage(resp, "Formulario de salud", pageHeader(req, "Formulario de salud") + flashMessage(req) +
				userCard(currentUser) +
				"<form method='post' action='" + req.getContextPath() + "/users/health'>" +
				input("Fecha del formulario", "intakeDate", "date", "") +
				textArea("Lesiones previas", "previousInjuries") +
				textArea("Enfermedades", "illnesses") +
				input("Nivel de actividad física", "physicalActivityLevel", "text", "Bajo, medio o alto") +
				input("Objetivo", "goal", "text", "Pérdida de peso, masa muscular, etc.") +
				select("Días disponibles", "availableDays", weekDaysOptions()) +
				button("Guardar formulario") +
				"</form>" +
				footerLinks(req, currentUser));
	}

	private void writeHomePage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User currentUser = currentUser(req);
		if (currentUser == null) {
			resp.sendRedirect(req.getContextPath() + "/users/login");
			return;
		}

		String roleText = currentUser.getRole() == null ? "Sin rol" : currentUser.getRole().name();
		String healthCount = String.valueOf(currentUser.getMedicalQuestionnaires().size());
		writePage(resp, "Inicio", pageHeader(req, "Bienvenido") + flashMessage(req) +
				userCard(currentUser) +
				"<div class='card'><p><strong>Rol:</strong> " + html(roleText) + "</p>" +
				"<p><strong>Formularios de salud:</strong> " + html(healthCount) + "</p>" +
				"<p><a href='" + req.getContextPath() + "/users/health'>Completar o actualizar formulario de salud</a></p>" +
				"<p><a href='" + req.getContextPath() + "/users/logout'>Cerrar sesión</a></p></div>" +
				footerLinks(req, currentUser));
	}

	private User buildUserFromRequest(HttpServletRequest req) {
		User user = new User();
		user.setFirstName(safeParam(req, "firstName"));
		user.setLastName(safeParam(req, "lastName"));
		user.setEmail(safeParam(req, "email"));
		user.setPassword(safeParam(req, "password"));
		user.setPhoneNumber(safeParam(req, "phoneNumber"));
		user.setGender(parseGender(req.getParameter("gender")));
		user.setBirthDate(parseDate(req.getParameter("birthDate")));
		user.setRole(parseRole(req.getParameter("role")));
		return user;
	}

	private IntakeQuestionnaire buildQuestionnaireFromRequest(HttpServletRequest req) {
		IntakeQuestionnaire questionnaire = new IntakeQuestionnaire();
		questionnaire.setIntakeDate(parseDate(req.getParameter("intakeDate")));
		questionnaire.setPreviousInjuries(safeParam(req, "previousInjuries"));
		questionnaire.setIllnesses(safeParam(req, "illnesses"));
		questionnaire.setPhysicalActivityLevel(safeParam(req, "physicalActivityLevel"));
		questionnaire.setGoal(safeParam(req, "goal"));
		questionnaire.setAvailableDays(parseWeekDay(req.getParameter("availableDays")));
		return questionnaire;
	}

	private String pageHeader(HttpServletRequest req, String title) {
		return "<div class='nav'>"
				+ link(req, "/users/login", "Login")
				+ link(req, "/users/register", "Registro")
				+ link(req, "/users/health", "Formulario de salud")
				+ link(req, "/users/home", "Inicio")
				+ link(req, "/users/logout", "Salir")
				+ "</div><h1>" + html(title) + "</h1>";
	}

	private String footerLinks(HttpServletRequest req, User currentUser) {
		StringBuilder builder = new StringBuilder("<div class='footer'>");
		if (currentUser == null) {
			builder.append(link(req, "/users/login", "Ya tengo cuenta"));
			builder.append(link(req, "/users/register", "Quiero registrarme"));
		} else {
			builder.append(link(req, "/users/home", "Ir al inicio"));
			builder.append(link(req, "/users/health", "Formulario de salud"));
		}
		builder.append("</div>");
		return builder.toString();
	}

	private String userCard(User user) {
		return "<div class='card'>"
				+ "<p><strong>Usuario:</strong> " + html(user.getFirstName() + " " + user.getLastName()) + "</p>"
				+ "<p><strong>Correo:</strong> " + html(user.getEmail()) + "</p>"
				+ "<p><strong>Rol:</strong> " + html(user.getRole() == null ? "" : user.getRole().name()) + "</p>"
				+ "</div>";
	}

	private void writePage(HttpServletResponse resp, String title, String body) throws IOException {
		try (PrintWriter out = resp.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html lang='es'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>");
			out.println("<title>" + html(title) + "</title>");
			out.println("<style>body{font-family:Arial,sans-serif;max-width:920px;margin:32px auto;padding:0 16px;} .nav a,.footer a{margin-right:12px;} .card{background:#f5f5f5;padding:16px;border-radius:8px;margin:16px 0;} label{display:block;margin-top:12px;font-weight:600;} input,select,textarea{width:100%;padding:10px;margin-top:6px;box-sizing:border-box;} textarea{min-height:100px;} .btn{margin-top:18px;padding:12px 18px;cursor:pointer;} .error{background:#fdd; color:#900; padding:12px;border-radius:6px;margin:12px 0;} .success{background:#e7f7e7;color:#135b13;padding:12px;border-radius:6px;margin:12px 0;}</style>");
			out.println("</head><body>");
			out.println(body);
			out.println("</body></html>");
		}
	}

	private String flashMessage(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return "";
		}
		String error = (String) session.getAttribute("flashError");
		String success = (String) session.getAttribute("flashSuccess");
		session.removeAttribute("flashError");
		session.removeAttribute("flashSuccess");
		if (error != null && !error.isBlank()) {
			return "<div class='error'>" + html(error) + "</div>";
		}
		if (success != null && !success.isBlank()) {
			return "<div class='success'>" + html(success) + "</div>";
		}
		return "";
	}

	private void sendFlash(HttpSession session, String type, String message) {
		if (session == null) {
			return;
		}
		if ("error".equals(type)) {
			session.setAttribute("flashError", message);
			session.removeAttribute("flashSuccess");
		} else {
			session.setAttribute("flashSuccess", message);
			session.removeAttribute("flashError");
		}
	}

	private String input(String label, String name, String type, String placeholder) {
		return "<label for='" + name + "'>" + html(label) + "</label>"
				+ "<input id='" + name + "' name='" + name + "' type='" + type + "' placeholder='" + html(placeholder) + "' required>";
	}

	private String textArea(String label, String name) {
		return "<label for='" + name + "'>" + html(label) + "</label>"
				+ "<textarea id='" + name + "' name='" + name + "' required></textarea>";
	}

	private String select(String label, String name, String options) {
		return "<label for='" + name + "'>" + html(label) + "</label>"
				+ "<select id='" + name + "' name='" + name + "' required>"
				+ options + "</select>";
	}

	private String button(String label) {
		return "<button class='btn' type='submit'>" + html(label) + "</button>";
	}

	private String link(HttpServletRequest req, String path, String text) {
		return "<a href='" + req.getContextPath() + path + "'>" + html(text) + "</a>";
	}

	private String gendersOptions() {
		return Arrays.stream(Gender.values())
				.map(gender -> "<option value='" + gender.name() + "'>" + genderLabel(gender) + "</option>")
				.collect(Collectors.joining());
	}

	private String rolesOptions() {
		return Stream.of(Role.TRAINER, Role.CLIENT)
				.map(role -> "<option value='" + role.name() + "'>" + roleLabel(role) + "</option>")
				.collect(Collectors.joining());
	}

	private String weekDaysOptions() {
		return Arrays.stream(WeekDay.values())
				.map(day -> "<option value='" + day.name() + "'>" + day.name() + "</option>")
				.collect(Collectors.joining());
	}

	private boolean isMissingRegistrationData(User user) {
		return user.getFirstName().isBlank()
				|| user.getLastName().isBlank()
				|| user.getEmail().isBlank()
				|| user.getPassword().isBlank()
				|| user.getPhoneNumber().isBlank()
				|| user.getGender() == null
				|| user.getBirthDate() == null
				|| user.getRole() == null;
	}

	private boolean isMissingHealthData(IntakeQuestionnaire questionnaire) {
		return questionnaire.getIntakeDate() == null
				|| questionnaire.getPreviousInjuries().isBlank()
				|| questionnaire.getIllnesses().isBlank()
				|| questionnaire.getPhysicalActivityLevel().isBlank()
				|| questionnaire.getGoal().isBlank()
				|| questionnaire.getAvailableDays() == null;
	}

	private User currentUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		Object value = session.getAttribute("currentUser");
		return value instanceof User user ? user : null;
	}

	private String normalizePath(String pathInfo) {
		if (pathInfo == null || pathInfo.isBlank() || "/".equals(pathInfo)) {
			return "/login";
		}
		return pathInfo;
	}

	private String safeParam(HttpServletRequest req, String name) {
		String value = req.getParameter(name);
		return value == null ? "" : value.trim();
	}

	private Gender parseGender(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return Gender.valueOf(value.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	private Role parseRole(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			Role role = Role.valueOf(value.toUpperCase(Locale.ROOT));
			return role == Role.ADMIN ? null : role;
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	private WeekDay parseWeekDay(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return WeekDay.valueOf(value.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	private LocalDate parseDate(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return LocalDate.parse(value);
		} catch (Exception ex) {
			return null;
		}
	}

	private String html(String text) {
		if (text == null) {
			return "";
		}
		return text.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}

	private String genderLabel(Gender gender) {
		return switch (gender) {
			case MALE -> "Hombre";
			case FEMALE -> "Mujer";
			case OTHER -> "Otro";
		};
	}

	private String roleLabel(Role role) {
		return switch (role) {
			case TRAINER -> "Entrenador";
			case CLIENT -> "Cliente";
			case ADMIN -> "Administrador";
		};
	}


}
