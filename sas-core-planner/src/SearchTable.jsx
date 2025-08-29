import React from 'react';
import { cores } from './config.js';


class SearchCell extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const coreOptions = cores.map(core => (
            <option key={core} value={core}>{core}</option>
        ));
        return (
            <React.Fragment>
                {!this.props.isFirst && <td>AND</td>}
                <td>
                    <select value={this.props.core} onChange={e => this.props.onChange(e.target.value)}>
                        {coreOptions}
                    </select>
                </td>
            </React.Fragment>
        );
    }
}

class SearchRow extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const searchCells = this.props.cores.map((core, index) => (
            <SearchCell 
                key={index} 
                core={core} 
                onChange={newCore => this.props.onChange(index, newCore)} 
                isFirst={index === 0} 
            />
        ));
        return (
            <tr>
                {this.props.firstRow && <td>OR</td>}
                {searchCells}
                <td><button onClick={this.props.onAdd}>AND</button></td>
                <td><button onClick={this.props.onDelete} disabled={this.props.cores.length === 1}>-</button></td>
            </tr>
        )
    }
}

class SearchTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            cores: [["CCD"]]
        }
    }
    addCore = (groupIndex) => {
        this.setState(prevState => {
            const cores = [...prevState.cores];
            cores[groupIndex] = [...cores[groupIndex], "CCD"];
            return { cores };
        });
    }
    removeCore = (groupIndex) => {
        this.setState(prevState => {
            const cores = [...prevState.cores];
            if(cores[groupIndex].length > 1) {
                cores[groupIndex].pop();
            }
            return { cores };
        });
    }
    addCoreGroup = () => {
        this.setState(prevState => ({
            cores: [...prevState.cores, ["CCD"]]
        }));
    }
    removeCoreGroup = () => {
        this.setState(prevState => {
            const cores = [...prevState.cores];
            cores.pop();
            return { cores };
        });
    }
    updateCore = (groupIndex, coreIndex, newCore) => {
        this.setState(prevState => {
            const cores = [...prevState.cores];
            cores[groupIndex][coreIndex] = newCore;
            return { cores };
        });
    }
    render() {
        const searchRows = this.state.cores.map((coreGroup, groupIndex) => (
            <SearchRow
                key={groupIndex}
                cores={coreGroup}
                onChange={(index, newCore) => this.updateCore(groupIndex, index, newCore)}
                onAdd={() => this.addCore(groupIndex)}
                onDelete={() => this.removeCore(groupIndex)}
            />
        ));
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th colSpan="100%">Search Cores</th>
                        </tr>
                    </thead>
                    <tbody>
                        {searchRows}
                    </tbody>
                </table>
                <div className="nav-actions">
                    <button onClick={() => this.addCoreGroup()}>OR</button>
                    <button onClick={() => this.removeCoreGroup()} disabled={this.state.cores.length === 1}>-</button>
                    <button onClick={() => this.props.onSearch(this.state.cores)}>Search</button>
                </div>
            </div>
        );
    }
}

export default SearchTable;