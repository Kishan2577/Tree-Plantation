package com.example.treeplantation;

public class getPlantation {
    private String id;
    private String treesplanted;
    private String plantedBy;
    private String treecount;

    // Constructors, getters, and setters
    public getPlantation(String id, String treecount, String plantedBy, String treesplanted) {
        this.id = id;
        this.treecount = treecount;
        this.plantedBy = plantedBy;
        this.treesplanted = treesplanted;
    }

    public String getId() { return id; }
    public String getTreeCount() { return treecount; }
    public String getPlantedBy() { return plantedBy; }
    public String getTreesPlanted() { return treesplanted; }
}
