import PropTypes from 'prop-types';
import React from 'react';
import Button from '../../ui/components/Button';
import toNiceDate from '../../ui/toNiceDate';

import FailuresTableContainer from './FailuresTableContainer';
import StatsProgressBar from '../../stats/components/StatsProgressBar';

export default class FailuresPage extends React.Component {

  static propTypes = {
    testRunId: PropTypes.string.isRequired,
    testRun: PropTypes.object,
    failures: PropTypes.object,
    stats: PropTypes.object,
    onLoad: PropTypes.func.isRequired,
    onReload: PropTypes.func.isRequired,
  };


  componentDidMount() {
    this.loadTestRunFailuresIfPossible();
  }

  componentDidUpdate(prevProps) {
    this.loadTestRunFailuresIfPossible(prevProps);
  }

  loadTestRunFailuresIfPossible(prevProps = {}) {
    const { testRunId } = this.props;

    if (testRunId !== prevProps.testRunId) {
      this.props.onLoad({ testRunId });
    }
  }

  onReload = () => {
    const { testRunId } = this.props;
    this.props.onReload({ testRunId });
  };

  render() {
    const { testRun, stats } = this.props;
    return (
      <div>
        <h1>
          Échecs
          {' '}
          <small>{`Tir du ${toNiceDate(testRun.date)}`}</small>
          <div style={{float: 'right'}}>
            <Button
              glyph="refresh"
              bsStyle="primary"
              bsSize="xsmall"
              onClick={this.onReload}>
              Actualiser
            </Button>
          </div>
        </h1>
        <StatsProgressBar stats={stats}/>
        <hr />
        <FailuresTableContainer />
      </div>
    );
  }

}
