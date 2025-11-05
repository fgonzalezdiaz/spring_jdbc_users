package com.ra2.users.ra2users.repositori;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import com.ra2.users.ra2users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import io.micrometer.observation.annotation.Observed;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException{
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setNom(rs.getString("nom"));
            user.setDesc(rs.getString("descr"));
            user.setEmail(rs.getString("email"));
            user.setPasswd(rs.getString("passwd"));
            user.setUltimAcces(rs.getTimestamp("ultimAcces"));
            user.setDataCreated(rs.getTimestamp("dataCreated"));
            user.setDataUpdated(rs.getTimestamp("dataUpdated"));
            return user;
        }
    }
    public List<User> findAll(){
        String sql = "Select * from users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public List<User> findOne(Long id){
        String sql = "Select * from users where id = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), id);
    }

    public int save(String nom, String desc, String email, String passwd, Timestamp ultimAcces, Timestamp dataCreated, Timestamp dataUpdated){
        String sql = "insert into users (nom, descr, email, passwd, ultimAcces, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, nom, desc, email, passwd, ultimAcces, dataCreated, dataUpdated);
    }

    public int updateUser(Long id, String nom, String desc, String email, String passwd){
        String sql = """
            UPDATE users
            SET nom = ?, 
            descr = ?, 
            email = ?, 
            passwd = ?, 
            ultimAcces = CURRENT_TIMESTAMP, 
            dataUpdated = CURRENT_TIMESTAMP
            WHERE id = ?
        """;
        return jdbcTemplate.update(sql, nom, desc, email, passwd, id);

    }

    public int updateNameUser(Long id, String name){
        String sql ="""
                Update users
                set nom = ?
                where id = ?
                """;
        return jdbcTemplate.update(sql, name, id);
    }

    public int deleteUser(Long id){
        String sql = """
                delete from users where id = ?
                """;
        return jdbcTemplate.update(sql, id);
    } 

}
