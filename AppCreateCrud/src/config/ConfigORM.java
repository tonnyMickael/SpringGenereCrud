package config;

import java.util.ArrayList;
import java.util.List;

public class ConfigORM {
	private String name_table_parent;
	private String name_table_child;
	private String assoc_parent_child;
	private String assoc_child_parent;
	private boolean isUniDirectionnal;
	private boolean isBiDirectionnal; 
	private String type_cascade; // ALL, PERSIT, MERGE,...
	private String ownerFkfield;
// (1): Do we want access the table parent(region) from the table child(person) 
// (2): Do we want access the table child(person) from the table parent(region)
	private String optionConfiguration;
	
	public String getOptionConfiguration() {
		return optionConfiguration;
	}
	public void setOptionConfiguration(String optionConfiguration) {
		this.optionConfiguration = optionConfiguration;
	}
	public boolean isUniDirectionnal() {
		return isUniDirectionnal;
	}
	public void setUniDirectionnal(boolean isUniDirectionnal) {
		this.isUniDirectionnal = isUniDirectionnal;
	}
	public boolean isBiDirectionnal() {
		return isBiDirectionnal;
	}
	public void setBiDirectionnal(boolean isBiDirectionnal) {
		this.isBiDirectionnal = isBiDirectionnal;
	}
	public String getName_table_parent() {
		return name_table_parent;
	}
	public void setName_table_parent(String name_table_parent) {
		this.name_table_parent = name_table_parent;
	}
	public String getName_table_child() {
		return name_table_child;
	}
	public void setName_table_child(String name_table_child) {
		this.name_table_child = name_table_child;
	}
	public String getAssoc_parent_child() {
		return assoc_parent_child;
	}
	public void setAssoc_parent_child(String assoc_parent_child) {
		this.assoc_parent_child = assoc_parent_child;
	}
	public String getAssoc_child_parent() {
		return assoc_child_parent;
	}
	public void setAssoc_child_parent(String assoc_child_parent) {
		this.assoc_child_parent = assoc_child_parent;
	}
	public String getType_cascade() {
		return type_cascade;
	}
	public void setType_cascade(String type_cascade) {
		this.type_cascade = type_cascade;
	}
	public String getOwnerFkfield() {
		return ownerFkfield;
	}
	public void setOwnerFkfield(String ownerFkfield) {
		this.ownerFkfield = ownerFkfield;
	}



	public ConfigORM(String name_table_parent, String name_table_child, String assoc_parent_child,
			String assoc_child_parent, boolean isUniDirectionnal, boolean isBiDirectionnal, String type_cascade,
			String optionConfiguration) {
		this.name_table_parent = name_table_parent;
		this.name_table_child = name_table_child;
		this.assoc_parent_child = assoc_parent_child;
		this.assoc_child_parent = assoc_child_parent;
		this.isUniDirectionnal = isUniDirectionnal;
		this.isBiDirectionnal = isBiDirectionnal;
		this.type_cascade = type_cascade;
		this.optionConfiguration = optionConfiguration;
	}
	public ConfigORM(){}


	public ConfigORM [] listORMConfig() {
		List<ConfigORM> list = new ArrayList<>();
		
		// @OneToOne unidirectional
		list.add(new ConfigORM("region", "person", "1-N", "1-1",true,false,"","2"));
		list.add(new ConfigORM("person", "address", "1-1", "1-1",true,false,"","2"));

		// Cascade
			// PERSIST
			// MERGE
			// DELETE
			// ALL
		// list.add(new ConfigORM("person", "address", "@OneToMany", "@ManyToOne", false, true));
		
		// Assocciation
		// 1-N sy 1-1
		// 1-1 sy 1-1
		// 1-N sy 1-N

		return list.toArray(new ConfigORM[0]);
	}
}
