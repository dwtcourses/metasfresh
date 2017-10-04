import counterpart from 'counterpart';
import PropTypes from 'prop-types';
import React, { Component } from 'react';

import {
    attachmentsRequest,
    openFile,
    deleteRequest
} from '../../actions/GenericActions';

import Loader from '../app/Loader';

class Attachments extends Component {
    state = {
        data: null,
        attachmentHovered: null,
        isAddUrlOpen: false,
    }

    componentDidMount = () => {
        const {windowType, docId} = this.props;

        attachmentsRequest('window', windowType, docId)
            .then(response => {
                this.setState({
                    data: response.data
                }, () => {
                    this.attachments && this.attachments.focus();
                })
            });
    }

    toggleAttachmentDelete = (value) => {
        this.setState({
            attachmentHovered: value
        })
    }

    handleAddUrlClick = () => {
        this.setState({ isAddUrlOpen: true });
    }

    handleAddUrlClose = (e) => {
        e.stopPropagation();
        this.setState({ isAddUrlOpen: false });
    }

    handleAttachmentClick = (id) => {
        const {windowType, docId} = this.props;
        openFile('window', windowType, docId, 'attachments', id);
    }

    handleAttachmentDelete = (e, id) => {
        const {windowType, docId} = this.props;
        e.stopPropagation();

        deleteRequest(
            'window', windowType, docId, null, null, 'attachments', id
        ).then(() => {
            return attachmentsRequest(
                'window', windowType, docId
            )
        }).then((response) => {
            this.setState({
                data: response.data
            })
        });
    }

    handleKeyDown = (e) => {
        const active = document.activeElement;

        const keyHandler = (e, dir) => {
            const sib = dir ? 'nextSibling' : 'previousSibling';
            e.preventDefault();
            if(active.classList.contains('js-subheader-item')){
                active[sib] && active[sib].focus();
            }else{
                active.childNodes[0].focus();
            }
        }

        switch(e.key){
            case 'ArrowDown':
                keyHandler(e, true);
                break
            case 'ArrowUp':
                keyHandler(e, false);
                break;
            case 'Enter':
                e.preventDefault();
                document.activeElement.click();
                break;
        }
    }

    renderActions = () => {
        const { isAddUrlOpen } = this.state;

        return (
            <div
              className="subheader-item js-subheader-item"
              onClick={this.handleAddUrlClick}
            >
                Add URL
                {/* TODO: find out how to add translations and use this instead:
                {counterpart.translate('window.sideList.attachments.addurl')}
                */}

                {isAddUrlOpen && (
                    <div onClick={this.handleAddUrlClose}>New URL</div>
                )}
            </div>
        );
    }

    renderData = () => {
        const { data, attachmentHovered } = this.state;

        return data.map((item, key) => (
            <div
                className="subheader-item subheader-item-ellipsis js-subheader-item"
                key={key}
                tabIndex={0}
                onMouseEnter={() => { this.toggleAttachmentDelete(item.id); }}
                onMouseLeave={() => { this.toggleAttachmentDelete(null); }}
                onClick={() => { this.handleAttachmentClick(item.id); }}
            >
                {item.name}
                {attachmentHovered === item.id &&
                    <div
                        className="subheader-additional-box"
                        onClick={(e) => {
                            this.handleAttachmentDelete(e, item.id);
                        }}
                    >
                        <i className="meta-icon-delete"/>
                    </div>
                }
            </div>
        ));
    }

    renderEmpty = () => (
        <div className="subheader-item subheader-item-disabled">
            {counterpart.translate('window.sideList.attachments.empty')}
        </div>
    )

    render() {
        const { data } = this.state;
        let actions, content;

        if (data) {
            content = data.length ? this.renderData() : this.renderEmpty();
            actions = this.renderActions();
        } else {
            content = <Loader />;
        }

        return (
            <div
                onKeyDown={this.handleKeyDown}
                ref={(c) => { this.attachments = c; }}
                tabIndex={0}
            >
                {content}
                {actions}
            </div>
        );
    }
}

Attachments.propTypes = {
    windowType: PropTypes.string.isRequired,
    docId: PropTypes.string.isRequired,
};

export default Attachments;
