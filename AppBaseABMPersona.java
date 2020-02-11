package basededatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AppBaseABMPersona {

	public static void main(String[] args) throws ClassNotFoundException {

		Connection conexion = null;
		try {
			conexion = AdminBD.obtenerConexion();
			Scanner sc = new Scanner(System.in);

			int opcion = mostrarMenu(sc);
			while (opcion != 0) {

				switch (opcion) {
				case 1:
					alta(conexion, sc);
					break;
				case 2:
					modificacion(conexion, sc);
					break;
				case 3:
					baja(conexion, sc);
					break;
				case 4:
					listado(conexion);
					break;
				case 0:

					break;

				default:
					break;
				}
				opcion = mostrarMenu(sc);
			}

			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void listado(Connection conexion) {
		System.out.println();
		System.out.println("LISTADO--------------------");
		System.out.println("ID-NOMBRE-----EDAD-----F.NACIM---------");
		Statement stmt;
		try {

			stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERSONA");
			while (rs.next()) {
				Date fNac = rs.getDate(4);
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);
			}

			System.out.println("FIN LISTADO------------");
			System.out.println();
		} catch (SQLException e) {

		}

	}
// 3.Baja

	private static void baja(Connection conexion, Scanner sc) throws ClassNotFoundException {

		System.out.println("BAJA DE REGISTRO");
		System.out.println("---------------");
		System.out.println("Ingrese ID:");
		int id = sc.nextInt();
		Statement stmt;

		try {

			conexion = AdminBD.obtenerConexion();
			stmt = conexion.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");

			while (rs.next()) {

				Date fNac = rs.getDate(4);
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);

				System.out.println("Esta seguro de que desea borrar estos datos?");
				System.out.println("1.SI| 2.NO");

				int op = sc.nextInt();

				switch (op) {
				case 1:

					stmt = conexion.createStatement();

					String insert = "DELETE FROM persona WHERE ID = " + id + ";";

					stmt.executeUpdate(insert);

					System.out.println("Se borro correctamente");

					listado(conexion);

					break;

				case 2:

					mostrarMenu(sc);

					break;

				default:

					break;

				}

			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();
			System.out.println("Se produjo un error intente nuevamente");

		}

	}

// 1:Alta

	private static void alta(Connection conexion, Scanner sc) throws ClassNotFoundException {
		System.out.println("ALTA DE PERSONA");
		System.out.println("---------------");
		System.out.println();
		System.out.println("Ingrese nombre:");
		String nombre = sc.next();
		System.out.println("Ingrese fecha nacimiento (aaaa-mm-dd):");
		String fNac = sc.next();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date fechaNac = sdf.parse(fNac);
			int edad = calcularEdad(fechaNac);
		} catch (ParseException e1) {

			e1.printStackTrace();
		}

		Statement stmt;

		try {

			Date fechaNac = sdf.parse(fNac);
			int edad = calcularEdad(fechaNac);
			conexion = AdminBD.obtenerConexion();
			stmt = conexion.createStatement();
			String insert = "INSERT INTO PERSONA (NOMBRE, EDAD, FECHA_NACIMIENTO) VALUES ('" + nombre + "', " + edad

					+ ", '" + fNac + "') ;";
			stmt.executeUpdate(insert);

			ResultSet rs = stmt.executeQuery("select * from persona");

			while (rs.next()) {

				int ID = rs.getInt(1);

				System.out.println(rs.getString(2) + "  " + rs.getInt(3) + "  " + rs.getDate(4));

			}

			conexion.close();

		} catch (ParseException | SQLException e) {

			System.out.println("No se cargaron los datos correctamente");

		}

	}

// Calculo de edad 

	private static int calcularEdad(Date fechaNac) {
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar hoy = new GregorianCalendar();
		gc.setTime(fechaNac);
		int anioActual = hoy.get(Calendar.YEAR);
		int anioNacim = gc.get(Calendar.YEAR);

		int mesActual = hoy.get(Calendar.MONTH);
		int mesNacim = gc.get(Calendar.MONTH);

		int diaActual = hoy.get(Calendar.DATE);
		int diaNacim = gc.get(Calendar.DATE);

		int dif = anioActual - anioNacim;

		if (mesActual < mesNacim) {
			dif = dif - 1;
		} else {
			if (mesActual == mesNacim && diaActual < diaNacim) {
				dif = dif - 1;
			}

		}
		return dif;
		
	}

	private static int mostrarMenu(Scanner sc) {

		System.out.println("SISTEMA DE PERSONAS (ABM)");
		System.out.println("=========================");

		System.out.println("");
		System.out.println("MENU OPCIONES: ");
		System.out.println("");
		System.out.println("1: ALTA ");
		System.out.println("2: MODIFICACION ");
		System.out.println("3: BAJA");
		System.out.println("4: LISTADO");
		System.out.println("0: SALIR");
		int opcion = 0;
		opcion = sc.nextInt();
		return opcion;
	}

	// 2. modificacion

	private static void modificacion(Connection conexion, Scanner sc) {

		System.out.println("MODIFICACION DE REGISTRO");
		System.out.println("---------------");
		System.out.println("Ingrese ID:");
		int id = sc.nextInt();
		Statement stmt;

		try {

			stmt = conexion.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");
			while (rs.next()) {

				Date fNac = rs.getDate(4);

				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);

			}

			System.out.println("1.Nombre , 2.Edad,  3.Fecha de nacimiento 4.Salir");
			int mod = sc.nextInt();

			switch (mod) {

			case 1:
				System.out.println("ingrese nuevo nombre:");
				String nueNom = sc.next();
				stmt = conexion.createStatement();
				String insert = "UPDATE persona SET NOMBRE = '" + nueNom + "' WHERE ID=" + id + ";";

				stmt.executeUpdate(insert);

				break;

			case 2:

				System.out.println("ingrese nueva edad:");
				int nueEd = sc.nextInt();
				stmt = conexion.createStatement();
				insert = "UPDATE persona SET EDAD =" + nueEd + " WHERE ID=" + id + ";";
				stmt.executeUpdate(insert);

				break;

			case 3:

				System.out.println("Ingrese fecha nacimiento (aaaa-mm-dd):");
				String fe = sc.next();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaNac = sdf.parse(fe);
				stmt = conexion.createStatement();
				insert = "UPDATE persona SET FECHA_NACIMIENTO ='" + fe + "' WHERE ID=" + id + ";";
				stmt.executeUpdate(insert);

				break;

			default:
				break;
			}

			rs = stmt.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");
			while (rs.next()) {
				System.out.println("El registro se modifico exitosamente");
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));

			}

			System.out.println("ingrese la columna que desea modificar");

			System.out.println("1.Nombre , 2.Edad,  3.Fecha de nacimiento 4.Salir");

			mod = sc.nextInt();

		} catch (SQLException e) {
			System.out.println("---------------------------------------");
			System.out.println("SE PRODUJO UN ERROR INTENTE NUEVAMENTE");
			System.out.println("---------------------------------------");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
