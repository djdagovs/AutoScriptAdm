/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entity.Columna;
import entity.Tabla;

/**
 *
 * @author Camilo
 */
public class ScriptHelper {

    public ScriptHelper() {
    }

    public String scriptCreateTable(Tabla tabla) {
        String query = "Prompt\n"
                + "Prompt Creando tabla " + tabla.getName() + "\n"
                + "Promp\n"
                + "--";
        query = "--\n"
                + "create table " + tabla.getName() + "(";
        for (Columna c : tabla.getColumnaCollection()) {
            if (c.isRequerido()) {
                query += "\n"+c.getNombre() + " " + c.getTipo() + " nn_" + tabla.getName() + "_" + c.getNombre().substring(c.getNombre().indexOf("_") + 1) + " not null,";
            } else {
                query += "\n"+c.getNombre() + " " + c.getTipo() + ",";
            }
        }
        query = quitarUltimaComa(query);
        query += ")\n"
                + "tablespace ts_dsfi\n"
                + "logging\n"
                + "nocompress\n"
                + "nocache\n"
                + "noparallel\n"
                + "monitoring;";
        return query;
    }

    public String scriptVersion(String descripcion) {
        String version = "--\n"
                + "-- #VERSION:0000001000\n"
                + "--\n"
                + "-- HISTORIAL DE CAMBIOS\n"
                + "--\n"
                + "-- Versión     GAP           Solicitud        Fecha        Realizó        Descripción\n"
                + "-- =========== ============= ================ ============ ============== ==============================================================================\n"
                + "-- 1000        XXXXX         XXXXXXXXX        XX/XX/XXXX                  . " + descripcion + "\n"
                + "-- =========== ============= ================ ============ ============== ==============================================================================\n"
                + "--\n";
        return version;
    }

    public String scriptTypeTable(Tabla tabla) {
        String query = "Prompt\n"
                + "Prompt Creando type para la tabla " + tabla.getName() + "\n"
                + "Promp\n"
                + "--";
        query = "--\n"
                + "type ty_" + tabla.getName() + " is record (";
        for (Columna c : tabla.getColumnaCollection()) {
            query += "\n"+c.getNombre() + " " + tabla.getName() + "." + c.getNombre()+ "%type,";
        }
        query = quitarUltimaComa(query);
        query += ");";
        return query;
    }

    public String scriptCrudTable(Tabla tabla) {
        String scriptPackage = "prompt\n"
                + "prompt PACKAGE: FA_QMAFA\n"
                + "prompt\n"
                + "\n"
                + "create or replace package XX_QXXX as\n"
                + "  procedure insertar_" + tabla.getName() + "   ( p_ty_" + tabla.getName() + " ty_" + tabla.getName() + ",\n"
                + "                              p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure actualizar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + " ty_" + tabla.getName() + ",\n"
                + "                               p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure eliminar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                             p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  end FA_QMAFA;\n"
                + "  /";
        String scriptBody = "create or replace package body XX_QXXXX as\n"
                + " ----------------------------------------------------------------------------------------------------\n"
                + "  procedure insertar_" + tabla.getName() + "   ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                              p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptInsertTable(tabla)
                + "\n"
                + "   p_ty_erro.cod_error := 'OK';\n"
                + "   p_ty_erro.msg_error := 'Inserción exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.insertar_" + tabla.getName() + ":'||sqlerrm, 1, 200);\n"
                + "  end insertar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure actualizar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                               p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptUpdateTable(tabla)
                + "\n"
                + "    p_ty_erro.cod_error := 'OK';\n"
                + "    p_ty_erro.msg_error := 'Actualización exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.actualizar_" + tabla.getName() + ":'||sqlerrm, 1, 200);\n"
                + "  end actualizar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure eliminar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                             p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptDeleteTable(tabla)
                + "\n"
                + "   p_ty_erro.cod_error := 'OK';\n"
                + "   p_ty_erro.msg_error := 'Eliminación exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.eliminar_" + tabla.getName() + ":'||sqlerrm, 1, 200);  \n"
                + "  end eliminar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "End XX_QXXXX;\n"
                + "/";

        return scriptPackage + scriptBody;
    }

    public String scriptInsertTable(Tabla tabla) {
        String script = "insert into " + tabla.getName() + "( \n";
        for (Columna c : tabla.getColumnaCollection()) {
            script += c.getNombre() + ",\n";
        }
        script = quitarUltimaComa(script);
        script += ")\n"
                + "values\n"
                + "( ";
        for (Columna c : tabla.getColumnaCollection()) {
            script += "\np_ty_" + tabla.getName() + "." + c.getNombre() + ",";
        }
        script = quitarUltimaComa(script);
        script += ");";
        return script;
    }

    public String scriptUpdateTable(Tabla tabla) {
        String script = "update " + tabla.getName() + " \nset";
        boolean primero = true;
        for (Columna c : tabla.getColumnaCollection()) {
            script += "\n"+c.getNombre() + " = " + "p_ty_" + tabla.getName() + "." + c.getNombre() + ",";
        }
        script = quitarUltimaComa(script);
        script += "\nwhere ";
        for (Columna c : tabla.getColumnaCollection()) {
            if(c.isLlavePrimaria()){
                if(primero){
                    script += "\n"+c.getNombre() +" = p_ty_" + tabla.getName() +"."+c.getNombre();
                    primero = false;
                }else{
                    script += "\nand "+c.getNombre() +" = p_ty_" + tabla.getName() +"."+c.getNombre();
                }
            }
            
        }
        script += "\n;";
        return script;
    }

    public String scriptDeleteTable(Tabla tabla) {
        boolean primero = true;
        String script = "delete " + tabla.getName() + " \n";
        script += "\nwhere ";
        for (Columna c : tabla.getColumnaCollection()) {
            if(c.isLlavePrimaria()){
                if(primero){
                    script += "\n"+c.getNombre() +" = p_ty_" + tabla.getName() +"."+c.getNombre();
                    primero = false;
                }else{
                    script += "\nand "+c.getNombre() +" = p_ty_" + tabla.getName() +"."+c.getNombre();
                }
            }            
        }
        script += "\n;";
        return "";
    }

    public String quitarUltimaComa(String query) {
        System.out.println("hola :D "+query.charAt(query.length() - 1));
        if (query.charAt(query.length() - 1) == ',') {            
            query = query.substring(0, query.length() - 1);
        }
        return query;
    }
}
