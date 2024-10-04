package com.example.treeplantation;

public class Plantation {
    private String id;
    private String treeCount;
    private String plantedBy;
    private String treesPlanted;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTreeCount() {
        return treeCount;
    }

    public void setTreeCount(String treeCount) {
        this.treeCount = treeCount;
    }

    public String getPlantedBy() {
        return plantedBy;
    }

    public void setPlantedBy(String plantedBy) {
        this.plantedBy = plantedBy;
    }

    public String getTreesPlanted() {
        return treesPlanted;
    }

    public void setTreesPlanted(String treesPlanted) {
        this.treesPlanted = treesPlanted;
    }
}
