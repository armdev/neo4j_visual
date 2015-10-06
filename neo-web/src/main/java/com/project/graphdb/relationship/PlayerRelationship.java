package com.project.graphdb.relationship;

import org.neo4j.graphdb.RelationshipType;


public enum PlayerRelationship implements RelationshipType {

    PLAY_IN,
   
    BELONGS_TO,

    COACH_OF;
   
    public static final String FROM = "from";
}
