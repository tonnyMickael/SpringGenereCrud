package config;

import java.util.ArrayList;
import java.util.List;

public class ConfigORM {
	private String table_name_parent;
	private String table_name_child;
	private String assoc_parent;
	private String assoc_child;
	private boolean isCascadeType ;
	private boolean isOrphanRemoval;

	public ConfigORM(){}
	
	public ConfigORM(String table_name_parent, String table_name_child, String assoc_parent, String assoc_child,
			boolean isCascadeType, boolean isOrphanRemoval) {
		super();
		this.table_name_parent = table_name_parent;
		this.table_name_child = table_name_child;
		this.assoc_parent = assoc_parent;
		this.assoc_child = assoc_child;
		this.isCascadeType = isCascadeType;
		this.isOrphanRemoval = isOrphanRemoval;
	}
	public String getTable_name_parent() {
		return table_name_parent;
	}
	public void setTable_name_parent(String table_name_parent) {
		this.table_name_parent = table_name_parent;
	}
	public String getTable_name_child() {
		return table_name_child;
	}
	public void setTable_name_child(String table_name_child) {
		this.table_name_child = table_name_child;
	}
	public String getAssoc_parent() {
		return assoc_parent;
	}
	public void setAssoc_parent(String assoc_parent) {
		this.assoc_parent = assoc_parent;
	}
	public String getAssoc_child() {
		return assoc_child;
	}
	public void setAssoc_child(String assoc_child) {
		this.assoc_child = assoc_child;
	}

	public boolean isOrphanRemoval() {
		return isOrphanRemoval;
	}

	public void setOrphanRemoval(boolean isOrphanRemoval) {
		this.isOrphanRemoval = isOrphanRemoval;
	}

	public boolean isCascadeType() {
		return isCascadeType;
	}

	public void setCascadeTypeAll(boolean isCascadeType) {
		this.isCascadeType = isCascadeType;
	}
	
	public ConfigORM [] listORMConfig() {
		List<ConfigORM> list = new ArrayList<>();
		
		// @OneToOne unidirectional
		list.add(new ConfigORM("region", "person", "@OneToMany", "", false, false));
		// list.add(new ConfigORM("person", "address", "@OneToMany", "@ManyToOne", false, true));
		
		return list.toArray(new ConfigORM[0]);
	}
}
