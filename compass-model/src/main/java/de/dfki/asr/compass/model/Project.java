/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel //the mixins change the data references, so that only ids or lists of ids will be delivered
public class Project extends AbstractCompassEntity implements Serializable {

	private static final long serialVersionUID = 8769137095832737487L;

	@Id
	@XmlTransient
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull(message = "Project needs a name.")
	@XmlElement
	@Column(length = 128, unique = true)
	@Size(min = 1, max = 128, message = "Maximum project name length is ${max} characters.")
	@Pattern(regexp = "^(?!\\s)[-\\w :.()]+(?<!\\s)$",
	         message = "Project name can only contain letters, dashes, braces, colons and dots. No leading or trailing spaces.")
	@ApiModelProperty("Name")
	private String name;

	@XmlElement(name = "scenario")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
	@ApiModelProperty(value = "List of ids of scenarios of the project", dataType = "Array[long]")
	private final List<Scenario> scenarios;

	@NotNull(message = "Project must have a PrefabSet.")
	@XmlElement
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "prefabSet_id", unique = true)
	@ApiModelProperty(value = "Id of the root prefabset", dataType = "long")
	private final PrefabSet prefabSet;

	public Project() {
		name = "";
		scenarios = new CopyOnWriteArrayList<>();
		prefabSet = new PrefabSet("Assets");
	}

	public Project(final String name) {
		this.name = name;
		scenarios = new CopyOnWriteArrayList<>();
		prefabSet = new PrefabSet("Assets");
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public void forceEagerFetch() {
		prefabSet.forceEagerFetch();
		for (Scenario s : scenarios) {
			s.forceEagerFetch();
		}
	}

	@Override
	public void clearIdsAfterDeepCopy() {
		setId(0);
		prefabSet.clearIdsAfterDeepCopy();
		for (Scenario s : scenarios) {
			s.clearIdsAfterDeepCopy();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void addScenario(final Scenario scenario) throws IllegalArgumentException {
		if (scenario == null) {
			throw new IllegalArgumentException(
					"Scenario to add to project was passed as null ");
		}

		if (scenario.getProject() == this) {
			scenarios.add(scenario);
		} else {
			scenario.setProject(this);
		}
	}

	public void removeScenario(final Scenario scenario) {
		if (scenario.getProject() == this) {
			scenario.setProject(null);
		} else {
			scenarios.remove(scenario);
		}
	}

	public List<Scenario> getScenarios() {
		return Collections.unmodifiableList(scenarios);
	}
	/*
	 * We prefer not to have a setScenarios() because it contradicts the unmodifiableList return of {@link #getScenarios()}.
	 * Setting of the scenarios field is handled by the {@link ScenarioListDeserializer}.
	 */

	public PrefabSet getPrefabSet() {
		return prefabSet;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + "] (" + name + ")";
	}

	public static class Comparators {

		public static final Comparator<Project> NAME = new Comparator<Project>() {

			@Override
			public int compare(final Project o1, final Project o2) {
				return o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
			}
		};
	}

}
